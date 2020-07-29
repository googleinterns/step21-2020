package com.google.sps.servlets;

import java.io.IOException;
import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;


/**
 * Runs when the grabComment() function in script.js called and provides a page with 
 * the comments image.
 */
@WebServlet("/servematch")
public class ServeMatchServlet extends HttpServlet {
  private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BlobKey mykey = new BlobKey(request.getParameter("key"));
    if (mykey != null && blobstoreService != null) {
      blobstoreService.serve(mykey, response);
    }
  }

}