package in.iitb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.common.collect.Lists;

import in.iitb.domain.ExtVideo;
import in.iitb.fileutils.Utils;
import in.iitb.repository.ExtVideoRepository;
import in.iitb.repository.search.ExtVideoSearchRepository;
import in.iitb.web.rest.util.Auth;
import in.iitb.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ExtVideo.
 */
@RestController
@RequestMapping("/api")
public class ExtVideoResource {

    private final Logger log = LoggerFactory.getLogger(ExtVideoResource.class);

    @Inject
    private ExtVideoRepository extVideoRepository;

    @Inject
    private ExtVideoSearchRepository extVideoSearchRepository;

    /**
     * POST  /extVideos -> Create a new extVideo.
     */
    @RequestMapping(value = "/extVideos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtVideo> createExtVideo(@Valid @RequestBody ExtVideo extVideo) throws URISyntaxException {
        log.debug("REST request to save ExtVideo : {}", extVideo);
        if (extVideo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new extVideo cannot already have an ID").body(null);
        }
        ExtVideo result = extVideoRepository.save(extVideo);
        extVideoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/extVideos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("extVideo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /extVideos -> Updates an existing extVideo.
     */
    @RequestMapping(value = "/extVideos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtVideo> updateExtVideo(@Valid @RequestBody ExtVideo extVideo) throws URISyntaxException {
        log.debug("REST request to update ExtVideo : {}", extVideo);
        if (extVideo.getId() == null) {
            return createExtVideo(extVideo);
        }
        ExtVideo result = extVideoRepository.save(extVideo);
        extVideoSearchRepository.save(extVideo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("extVideo", extVideo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /extVideos -> get all the extVideos.
     */
    @RequestMapping(value = "/extVideos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExtVideo> getAllExtVideos(@RequestParam(required = false) String filter) {
        if ("extvideotutorial-is-null".equals(filter)) {
            log.debug("REST request to get all ExtVideos where extVideoTutorial is null");
            return StreamSupport
                .stream(extVideoRepository.findAll().spliterator(), false)
                .filter(extVideo -> extVideo.getExtVideoTutorial() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all ExtVideos");
        return extVideoRepository.findAll();
    }

    /**
     * GET  /extVideos/:id -> get the "id" extVideo.
     */
    @RequestMapping(value = "/extVideos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtVideo> getExtVideo(@PathVariable Long id) {
        log.debug("REST request to get ExtVideo : {}", id);
        return Optional.ofNullable(extVideoRepository.findOne(id))
            .map(extVideo -> new ResponseEntity<>(
                extVideo,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /extVideos/:id -> delete the "id" extVideo.
     */
    @RequestMapping(value = "/extVideos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExtVideo(@PathVariable Long id) {
        log.debug("REST request to delete ExtVideo : {}", id);
        extVideoRepository.delete(id);
        extVideoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("extVideo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/extVideos/:query -> search for the extVideo corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/extVideos/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExtVideo> searchExtVideos(@PathVariable String query) {
        return StreamSupport
            .stream(extVideoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

        /**
     * POST  /extVideos -> UPLOAD a new extVideo.
     */
    @Async
    @RequestMapping(value = "/extVideos/upload",
    		 headers = "content-type=multipart/*")
    public String uploadExtVideo(@RequestParam("video") MultipartFile file) throws URISyntaxException {

		MultipartFile mFile = file;
		String fileName = mFile.getOriginalFilename();
		File temp = Utils.saveFile("a.mp4",Utils.getTempVideoDir() , mFile);
		File serverFile = new File(Utils.getVideoDir() +File.separator+ fileName);
		Random randomint = new Random();
		int flag=1;
		do{
			try 
			{
				Files.copy(temp.toPath(), serverFile.toPath());
				flag=1;
			}	
			catch (FileAlreadyExistsException e)
			{
				System.out.println("File already exist. Renaming file and trying again.");
				fileName = fileName.substring(0,fileName.length()-4);
				fileName = fileName + "_" + Integer.toString(randomint.nextInt()) + ".mp4";
				serverFile = new File(Utils.getVideoDir() +File.separator+ fileName);
				flag=0;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}while(flag==0);
		//printStimes(100);
		uploadToYoutube(serverFile.getAbsolutePath(),temp.getAbsolutePath());
		String url = Utils.getVideoDirURL() + fileName;
		System.out.println(url);
		return url;
    }
    
   
    
    
    
    private static YouTube youtube;

    /**
     * Define a global variable that specifies the MIME type of the video
     * being uploaded.
     */
    private static final String VIDEO_FILE_FORMAT = "video/*";

    private static final String SAMPLE_VIDEO_FILENAME = "sample-video.mp4";
    
    
    @Async 
    public void uploadToYoutube(String trueVideopath, String tempVideopath)
    {
    	// This OAuth 2.0 access scope allows an application to upload files
        // to the authenticated user's YouTube channel, but doesn't allow
        // other types of access.
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");

        try {
            // Authorize the request.
            Credential credential = Auth.authorize(scopes, "uploadvideo");

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                    "youtube-cmdline-uploadvideo-sample").build();

            System.out.println("Uploading: " + SAMPLE_VIDEO_FILENAME);

            // Add extra information to the video before uploading.
            Video videoObjectDefiningMetadata = new Video();

            // Set the video to be publicly visible. This is the default
            // setting. Other supporting settings are "unlisted" and "private."
            VideoStatus status = new VideoStatus();
            status.setPrivacyStatus("public");
            videoObjectDefiningMetadata.setStatus(status);

            // Most of the video's metadata is set on the VideoSnippet object.
            VideoSnippet snippet = new VideoSnippet();

            // This code uses a Calendar instance to create a unique name and
            // description for test purposes so that you can easily upload
            // multiple files. You should remove this code from your project
            // and use your own standard names instead.
            Calendar cal = Calendar.getInstance();
            snippet.setTitle("Test Upload via Java on " + cal.getTime());
            snippet.setDescription(
                    "Video uploaded via YouTube Data API V3 using the Java library " + "on " + cal.getTime());

            // Set the keyword tags that you want to associate with the video.
            List<String> tags = new ArrayList<String>();
            tags.add("test");
            tags.add("example");
            tags.add("java");
            tags.add("YouTube Data API V3");
            tags.add("erase me");
            snippet.setTags(tags);

            // Add the completed snippet object to the video resource.
            videoObjectDefiningMetadata.setSnippet(snippet);
            InputStream inputStream = new FileInputStream(trueVideopath);
            InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT,inputStream);

            // Insert the video. The command sends three arguments. The first
            // specifies which information the API request is setting and which
            // information the API response should return. The second argument
            // is the video resource that contains metadata about the new video.
            // The third argument is the actual video content.
            YouTube.Videos.Insert videoInsert = youtube.videos()
                    .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);

            // Set the upload type and add an event listener.
            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

            // Indicate whether direct media upload is enabled. A value of
            // "True" indicates that direct media upload is enabled and that
            // the entire media content will be uploaded in a single request.
            // A value of "False," which is the default, indicates that the
            // request will use the resumable media upload protocol, which
            // supports the ability to resume an upload operation after a
            // network interruption or other transmission failure, saving
            // time and bandwidth in the event of network failures.
            uploader.setDirectUploadEnabled(false);

            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            System.out.println("Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            System.out.println("Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            System.out.println("Upload in progress");
                            System.out.println("Upload percentage: " + uploader.getProgress());
                            break;
                        case MEDIA_COMPLETE:
                            System.out.println("Upload Completed!");
                            break;
                        case NOT_STARTED:
                            System.out.println("Upload Not Started!");
                            break;
                    }
                }
            };
            uploader.setProgressListener(progressListener);

            // Call the API and upload the video.
            Video returnedVideo = videoInsert.execute();

            // Print data about the newly inserted video from the API response.
            System.out.println("\n================== Returned Video ==================\n");
            System.out.println("  - Id: " + returnedVideo.getId());
            System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
            System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
            System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
            System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());

        } catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
        
        File temp= new File(tempVideopath);
        temp.delete();
        File trueVideo= new File(trueVideopath);
        trueVideo.delete();
        
    }


}
