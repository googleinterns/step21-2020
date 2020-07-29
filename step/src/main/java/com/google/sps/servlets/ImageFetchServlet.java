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

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * When chat.html is loaded it makes a call to this servlet to get an uploadURL
 * for the users image.
 */
@WebServlet("/image-upload")
public class ImageFetchServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      BlobstoreService myBlobService = BlobstoreServiceFactory.getBlobstoreService();
      String uploadUrl = "";

      if (myBlobService != null) {
        uploadUrl = myBlobService.createUploadUrl("/blobstore-upload-url");
      } 

      if (uploadUrl == null) {
        uploadUrl = "";
      }


      response.setContentType("text/html");
      response.getWriter().println(uploadUrl);
  }
}