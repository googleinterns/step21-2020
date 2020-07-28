// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.List;
import java.util.Map;
import com.google.sps.DatabaseHandler;
import org.json.simple.JSONObject;

@WebServlet("/blobstore-upload-url")
public class ImageUploadServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      System.out.println("inside get upload servlet");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("inside doPost!!!!!!!!!!!!!!!!!!!!!!!!");

    UserService userService = UserServiceFactory.getUserService();
    String id = userService.getCurrentUser().getUserId();
    String imageBlobKey = filterAndUploadImageBlobstore(request);

    if (imageBlobKey == null) {
      System.err.println("Error occured while uploading user's image");
      // TODO: Display some message on the screen telling the user to check the file
      //       they uploaded and try again.
    } else {
      DatabaseHandler.uploadUserImage(id, imageBlobKey);
    }
    /// line below this is causing an "error 405: HTTP method GET is not supported by this url"
    response.sendRedirect("profile.jsp");
  }

  private String filterAndUploadImageBlobstore(HttpServletRequest request){
    BlobstoreService myBlobService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>>  blobs = myBlobService.getUploads(request);
    List<BlobKey> myKeys = blobs.get("image");

    if (myKeys == null || myKeys.isEmpty()) {
      return null;
    }

    BlobKey imageBlobKey = myKeys.get(0);
    // Checks if user selected a file to upload
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(imageBlobKey);
    if (blobInfo.getSize() == 0) {
      myBlobService.delete(imageBlobKey);
      return null;
    }

    // Checks if file type is an image
    String type = blobInfo.getContentType();
    if (!type.startsWith("image")){
      myBlobService.delete(imageBlobKey);
      return null;
    }

    return imageBlobKey.getKeyString();    
  }

}
