
            package com.hoddmimes.distributor.generated;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.io.IOException;




    import org.bson.BsonType;
    import org.bson.Document;
    import org.bson.conversions.Bson;
    import com.mongodb.BasicDBObject;
    import org.bson.types.ObjectId;
    import com.hoddmimes.jsontransform.MessageMongoInterface;
    import com.hoddmimes.jsontransform.MongoDecoder;
    import com.hoddmimes.jsontransform.MongoEncoder;


import com.hoddmimes.jsontransform.MessageInterface;
import com.hoddmimes.jsontransform.JsonDecoder;
import com.hoddmimes.jsontransform.JsonEncoder;
import com.hoddmimes.jsontransform.ListFactory;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



            

            @SuppressWarnings({"WeakerAccess","unused","unchecked"})
            public class ErrorRsp implements MessageInterface 
            {
            
                    private Integer mRqstId;
                    private String mMessage;
                    private String mException;
               public ErrorRsp()
               {
                
               }

               public ErrorRsp(String pJsonString ) {
                    
                    JsonDecoder tDecoder = new JsonDecoder( pJsonString );
                    this.decode( tDecoder );
               }
    
            public ErrorRsp setRqstId( Integer pRqstId ) {
            mRqstId = pRqstId;
            return this;
            }
            public Optional<Integer> getRqstId() {
              return  Optional.ofNullable(mRqstId);
            }
        
            public ErrorRsp setMessage( String pMessage ) {
            mMessage = pMessage;
            return this;
            }
            public Optional<String> getMessage() {
              return  Optional.ofNullable(mMessage);
            }
        
            public ErrorRsp setException( String pException ) {
            mException = pException;
            return this;
            }
            public Optional<String> getException() {
              return  Optional.ofNullable(mException);
            }
        

        public String getMessageName() {
        return "ErrorRsp";
        }
    

        public JsonObject toJson() {
            JsonEncoder tEncoder = new JsonEncoder();
            this.encode( tEncoder );
            return tEncoder.toJson();
        }

        
        public void encode( JsonEncoder pEncoder) {

        
            JsonEncoder tEncoder = new JsonEncoder();
            pEncoder.add("ErrorRsp", tEncoder.toJson() );
            //Encode Attribute: mRqstId Type: int List: false
            tEncoder.add( "rqstId", mRqstId );
        
            //Encode Attribute: mMessage Type: String List: false
            tEncoder.add( "message", mMessage );
        
            //Encode Attribute: mException Type: String List: false
            tEncoder.add( "exception", mException );
        
        }

        
        public void decode( JsonDecoder pDecoder) {

        
            JsonDecoder tDecoder = pDecoder.get("ErrorRsp");
        
            //Decode Attribute: mRqstId Type:int List: false
            mRqstId = tDecoder.readInteger("rqstId");
        
            //Decode Attribute: mMessage Type:String List: false
            mMessage = tDecoder.readString("message");
        
            //Decode Attribute: mException Type:String List: false
            mException = tDecoder.readString("exception");
        

        }
    

        @Override
        public String toString() {
             Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create();
             return  gsonPrinter.toJson( this.toJson());
        }
    

        public static  Builder getErrorRspBuilder() {
            return new ErrorRsp.Builder();
        }


        public static class  Builder {
          private ErrorRsp mInstance;

          private Builder () {
            mInstance = new ErrorRsp();
          }

        
                        public Builder setRqstId( Integer pValue ) {
                        mInstance.setRqstId( pValue );
                        return this;
                    }
                
                        public Builder setMessage( String pValue ) {
                        mInstance.setMessage( pValue );
                        return this;
                    }
                
                        public Builder setException( String pValue ) {
                        mInstance.setException( pValue );
                        return this;
                    }
                

        public ErrorRsp build() {
            return mInstance;
        }

        }
    
            }
            
        /**
            Possible native attributes
            o "boolean" mapped to JSON "Boolean"
            o "byte" mapped to JSON "Integer"
            o "char" mapped to JSON "Integer"
            o "short" mapped to JSON "Integer"
            o "int" mapped to JSON "Integer"
            o "long" mapped to JSON "Integer"
            o "double" mapped to JSON "Numeric"
            o "String" mapped to JSON "String"
            o "byte[]" mapped to JSON "String" (Base64 string)


            An attribute could also be an "constant" i.e. having the property "constantGroup", should then refer to an defined /Constang/Group
             conastants are mapped to JSON strings,


            If the type is not any of the types below it will be refer to an other structure / object

        **/

    