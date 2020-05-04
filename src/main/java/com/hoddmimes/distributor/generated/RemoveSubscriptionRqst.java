
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
            public class RemoveSubscriptionRqst implements MessageInterface 
            {
            
                    private Integer mRqstId;
                    private Long mHandle;
                    private Integer mGroupId;
               public RemoveSubscriptionRqst()
               {
                
               }

               public RemoveSubscriptionRqst(String pJsonString ) {
                    
                    JsonDecoder tDecoder = new JsonDecoder( pJsonString );
                    this.decode( tDecoder );
               }
    
            public RemoveSubscriptionRqst setRqstId( Integer pRqstId ) {
            mRqstId = pRqstId;
            return this;
            }
            public Optional<Integer> getRqstId() {
              return  Optional.ofNullable(mRqstId);
            }
        
            public RemoveSubscriptionRqst setHandle( Long pHandle ) {
            mHandle = pHandle;
            return this;
            }
            public Optional<Long> getHandle() {
              return  Optional.ofNullable(mHandle);
            }
        
            public RemoveSubscriptionRqst setGroupId( Integer pGroupId ) {
            mGroupId = pGroupId;
            return this;
            }
            public Optional<Integer> getGroupId() {
              return  Optional.ofNullable(mGroupId);
            }
        

        public String getMessageName() {
        return "RemoveSubscriptionRqst";
        }
    

        public JsonObject toJson() {
            JsonEncoder tEncoder = new JsonEncoder();
            this.encode( tEncoder );
            return tEncoder.toJson();
        }

        
        public void encode( JsonEncoder pEncoder) {

        
            JsonEncoder tEncoder = new JsonEncoder();
            pEncoder.add("RemoveSubscriptionRqst", tEncoder.toJson() );
            //Encode Attribute: mRqstId Type: int List: false
            tEncoder.add( "rqstId", mRqstId );
        
            //Encode Attribute: mHandle Type: long List: false
            tEncoder.add( "handle", mHandle );
        
            //Encode Attribute: mGroupId Type: int List: false
            tEncoder.add( "groupId", mGroupId );
        
        }

        
        public void decode( JsonDecoder pDecoder) {

        
            JsonDecoder tDecoder = pDecoder.get("RemoveSubscriptionRqst");
        
            //Decode Attribute: mRqstId Type:int List: false
            mRqstId = tDecoder.readInteger("rqstId");
        
            //Decode Attribute: mHandle Type:long List: false
            mHandle = tDecoder.readLong("handle");
        
            //Decode Attribute: mGroupId Type:int List: false
            mGroupId = tDecoder.readInteger("groupId");
        

        }
    

        @Override
        public String toString() {
             Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create();
             return  gsonPrinter.toJson( this.toJson());
        }
    

        public static  Builder getRemoveSubscriptionRqstBuilder() {
            return new RemoveSubscriptionRqst.Builder();
        }


        public static class  Builder {
          private RemoveSubscriptionRqst mInstance;

          private Builder () {
            mInstance = new RemoveSubscriptionRqst();
          }

        
                        public Builder setRqstId( Integer pValue ) {
                        mInstance.setRqstId( pValue );
                        return this;
                    }
                
                        public Builder setHandle( Long pValue ) {
                        mInstance.setHandle( pValue );
                        return this;
                    }
                
                        public Builder setGroupId( Integer pValue ) {
                        mInstance.setGroupId( pValue );
                        return this;
                    }
                

        public RemoveSubscriptionRqst build() {
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

    