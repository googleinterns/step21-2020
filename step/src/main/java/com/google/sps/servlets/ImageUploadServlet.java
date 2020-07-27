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
import javax.servlet.http.HttpServletResponse;import com.google.appengine.api.blobstore.BlobInfo;
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
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("do post");

    UserService userService = UserServiceFactory.getUserService();
    System.out.println("1");
    String id = userService.getCurrentUser().getUserId();
    System.out.println("2");
    String imageBlobKey = filterAndUploadImageBlobstore(request);
    System.out.println("3");

    if (imageBlobKey == null) {
      System.out.println("is null");
      System.err.println("Error occured while uploading user's image");
      // TODO: Display some message on the screen telling the user to check the file
      //       they uploaded and try again.
    } else {
      System.out.println("is not null");
      DatabaseHandler.uploadUserImage(id, imageBlobKey);
    }
    System.out.println("sending redirect");
    response.sendRedirect("profile.jsp");
  }

  private String filterAndUploadImageBlobstore(HttpServletRequest request){
    System.out.println("inside filter and upload");
    BlobstoreService myBlobService = BlobstoreServiceFactory.getBlobstoreService();
    System.out.println("a");
    Map<String, List<BlobKey>>  blobs = myBlobService.getUploads(request);
    System.out.println("b");
    List<BlobKey> myKeys = blobs.get("image");
    System.out.println("c");

    if (myKeys == null || myKeys.isEmpty()) {
      System.out.println("returning null 1");
      return null;
    }

    BlobKey imageBlobKey = myKeys.get(0);
    System.out.println("d");
    // Checks if user selected a file to upload
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(imageBlobKey);
    System.out.println("e");
    if (blobInfo.getSize() == 0) {
      System.out.println("f");
      myBlobService.delete(imageBlobKey);
      System.out.println("g");
      return null;
    }

    // Checks if file type is an image
    System.out.println("h");
    String type = blobInfo.getContentType();
    System.out.println("i");
    if (!type.startsWith("image")){
      System.out.println("j");
      myBlobService.delete(imageBlobKey);
      System.out.println("k");
      return null;
    }

    System.out.println("l");
    return imageBlobKey.getKeyString();    
  }

}
