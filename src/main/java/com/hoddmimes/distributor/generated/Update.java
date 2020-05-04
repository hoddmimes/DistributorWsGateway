
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
            public class Update implements MessageInterface 
            {
            
                    private String mSubjectId;
                    private Long mHandle;
                    private String mTime;
                    private Integer mDistQueLen;
                    private Integer mSndrQueLen;
                    private String mPayload;
                    private String mCallbackRef;
               public Update()
               {
                
               }

               public Update(String pJsonString ) {
                    
                    JsonDecoder tDecoder = new JsonDecoder( pJsonString );
                    this.decode( tDecoder );
               }
    
            public Update setSubjectId( String pSubjectId ) {
            mSubjectId = pSubjectId;
            return this;
            }
            public Optional<String> getSubjectId() {
              return  Optional.ofNullable(mSubjectId);
            }
        
            public Update setHandle( Long pHandle ) {
            mHandle = pHandle;
            return this;
            }
            public Optional<Long> getHandle() {
              return  Optional.ofNullable(mHandle);
            }
        
            public Update setTime( String pTime ) {
            mTime = pTime;
            return this;
            }
            public Optional<String> getTime() {
              return  Optional.ofNullable(mTime);
            }
        
            public Update setDistQueLen( Integer pDistQueLen ) {
            mDistQueLen = pDistQueLen;
            return this;
            }
            public Optional<Integer> getDistQueLen() {
              return  Optional.ofNullable(mDistQueLen);
            }
        
            public Update setSndrQueLen( Integer pSndrQueLen ) {
            mSndrQueLen = pSndrQueLen;
            return this;
            }
            public Optional<Integer> getSndrQueLen() {
              return  Optional.ofNullable(mSndrQueLen);
            }
        
            public Update setPayload( String pPayload ) {
            mPayload = pPayload;
            return this;
            }
            public Optional<String> getPayload() {
              return  Optional.ofNullable(mPayload);
            }
        
            public Update setCallbackRef( String pCallbackRef ) {
            mCallbackRef = pCallbackRef;
            return this;
            }
            public Optional<String> getCallbackRef() {
              return  Optional.ofNullable(mCallbackRef);
            }
        

        public String getMessageName() {
        return "Update";
        }
    

        public JsonObject toJson() {
            JsonEncoder tEncoder = new JsonEncoder();
            this.encode( tEncoder );
            return tEncoder.toJson();
        }

        
        public void encode( JsonEncoder pEncoder) {

        
            JsonEncoder tEncoder = new JsonEncoder();
            pEncoder.add("Update", tEncoder.toJson() );
            //Encode Attribute: mSubjectId Type: String List: false
            tEncoder.add( "subjectId", mSubjectId );
        
            //Encode Attribute: mHandle Type: long List: false
            tEncoder.add( "handle", mHandle );
        
            //Encode Attribute: mTime Type: String List: false
            tEncoder.add( "time", mTime );
        
            //Encode Attribute: mDistQueLen Type: int List: false
            tEncoder.add( "distQueLen", mDistQueLen );
        
            //Encode Attribute: mSndrQueLen Type: int List: false
            tEncoder.add( "sndrQueLen", mSndrQueLen );
        
            //Encode Attribute: mPayload Type: String List: false
            tEncoder.add( "payload", mPayload );
        
            //Encode Attribute: mCallbackRef Type: String List: false
            tEncoder.add( "callbackRef", mCallbackRef );
        
        }

        
        public void decode( JsonDecoder pDecoder) {

        
            JsonDecoder tDecoder = pDecoder.get("Update");
        
            //Decode Attribute: mSubjectId Type:String List: false
            mSubjectId = tDecoder.readString("subjectId");
        
            //Decode Attribute: mHandle Type:long List: false
            mHandle = tDecoder.readLong("handle");
        
            //Decode Attribute: mTime Type:String List: false
            mTime = tDecoder.readString("time");
        
            //Decode Attribute: mDistQueLen Type:int List: false
            mDistQueLen = tDecoder.readInteger("distQueLen");
        
            //Decode Attribute: mSndrQueLen Type:int List: false
            mSndrQueLen = tDecoder.readInteger("sndrQueLen");
        
            //Decode Attribute: mPayload Type:String List: false
            mPayload = tDecoder.readString("payload");
        
            //Decode Attribute: mCallbackRef Type:String List: false
            mCallbackRef = tDecoder.readString("callbackRef");
        

        }
    

        @Override
        public String toString() {
             Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create();
             return  gsonPrinter.toJson( this.toJson());
        }
    

        public static  Builder getUpdateBuilder() {
            return new Update.Builder();
        }


        public static class  Builder {
          private Update mInstance;

          private Builder () {
            mInstance = new Update();
          }

        
                        public Builder setSubjectId( String pValue ) {
                        mInstance.setSubjectId( pValue );
                        return this;
                    }
                
                        public Builder setHandle( Long pValue ) {
                        mInstance.setHandle( pValue );
                        return this;
                    }
                
                        public Builder setTime( String pValue ) {
                        mInstance.setTime( pValue );
                        return this;
                    }
                
                        public Builder setDistQueLen( Integer pValue ) {
                        mInstance.setDistQueLen( pValue );
                        return this;
                    }
                
                        public Builder setSndrQueLen( Integer pValue ) {
                        mInstance.setSndrQueLen( pValue );
                        return this;
                    }
                
                        public Builder setPayload( String pValue ) {
                        mInstance.setPayload( pValue );
                        return this;
                    }
                
                        public Builder setCallbackRef( String pValue ) {
                        mInstance.setCallbackRef( pValue );
                        return this;
                    }
                

        public Update build() {
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

    