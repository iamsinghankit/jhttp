package com.singhankit.jhttp.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Ankit Singh
 */
  class TCPHandler {

      private static final Logger LOG = LoggerFactory.getLogger(TCPHandler.class);
      private final Request req;

      TCPHandler(Request req){
          this.req=req;
      }

      void writeSocket(String response){
          try{
              req.out().write(response);
              req.out().flush();
          }catch(IOException ex){
              LOG.error("Error occurred while writing to socket", ex);
          }
      }
}
