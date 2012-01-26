// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: location.proto

package com.cloudlbs.platform.protocol;

public final class LocationProto {
  private LocationProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registry.add(com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage.items);
  }
  public interface FixedLocationMessageOrBuilder extends
      com.google.protobuf.GeneratedMessage.
          ExtendableMessageOrBuilder<FixedLocationMessage> {
    
    // optional string guid = 1;
    boolean hasGuid();
    String getGuid();
    
    // optional string label = 2;
    boolean hasLabel();
    String getLabel();
    
    // optional double latitude = 3;
    boolean hasLatitude();
    double getLatitude();
    
    // optional double longitude = 4;
    boolean hasLongitude();
    double getLongitude();
    
    // optional double altitude = 5;
    boolean hasAltitude();
    double getAltitude();
    
    // optional int64 createDate = 6;
    boolean hasCreateDate();
    long getCreateDate();
    
    // optional .scope.ScopeMessage scope = 7;
    boolean hasScope();
    com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage getScope();
    com.cloudlbs.platform.protocol.ScopeProto.ScopeMessageOrBuilder getScopeOrBuilder();
    
    // optional int64 version = 8;
    boolean hasVersion();
    long getVersion();
  }
  public static final class FixedLocationMessage extends
      com.google.protobuf.GeneratedMessage.ExtendableMessage<
        FixedLocationMessage> implements FixedLocationMessageOrBuilder {
    // Use FixedLocationMessage.newBuilder() to construct.
    private FixedLocationMessage(Builder builder) {
      super(builder);
    }
    private FixedLocationMessage(boolean noInit) {}
    
    private static final FixedLocationMessage defaultInstance;
    public static FixedLocationMessage getDefaultInstance() {
      return defaultInstance;
    }
    
    public FixedLocationMessage getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.cloudlbs.platform.protocol.LocationProto.internal_static_location_FixedLocationMessage_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.cloudlbs.platform.protocol.LocationProto.internal_static_location_FixedLocationMessage_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional string guid = 1;
    public static final int GUID_FIELD_NUMBER = 1;
    private java.lang.Object guid_;
    public boolean hasGuid() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getGuid() {
      java.lang.Object ref = guid_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          guid_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getGuidBytes() {
      java.lang.Object ref = guid_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        guid_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string label = 2;
    public static final int LABEL_FIELD_NUMBER = 2;
    private java.lang.Object label_;
    public boolean hasLabel() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getLabel() {
      java.lang.Object ref = label_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          label_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getLabelBytes() {
      java.lang.Object ref = label_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        label_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional double latitude = 3;
    public static final int LATITUDE_FIELD_NUMBER = 3;
    private double latitude_;
    public boolean hasLatitude() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public double getLatitude() {
      return latitude_;
    }
    
    // optional double longitude = 4;
    public static final int LONGITUDE_FIELD_NUMBER = 4;
    private double longitude_;
    public boolean hasLongitude() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public double getLongitude() {
      return longitude_;
    }
    
    // optional double altitude = 5;
    public static final int ALTITUDE_FIELD_NUMBER = 5;
    private double altitude_;
    public boolean hasAltitude() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public double getAltitude() {
      return altitude_;
    }
    
    // optional int64 createDate = 6;
    public static final int CREATEDATE_FIELD_NUMBER = 6;
    private long createDate_;
    public boolean hasCreateDate() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public long getCreateDate() {
      return createDate_;
    }
    
    // optional .scope.ScopeMessage scope = 7;
    public static final int SCOPE_FIELD_NUMBER = 7;
    private com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage scope_;
    public boolean hasScope() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    public com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage getScope() {
      return scope_;
    }
    public com.cloudlbs.platform.protocol.ScopeProto.ScopeMessageOrBuilder getScopeOrBuilder() {
      return scope_;
    }
    
    // optional int64 version = 8;
    public static final int VERSION_FIELD_NUMBER = 8;
    private long version_;
    public boolean hasVersion() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    public long getVersion() {
      return version_;
    }
    
    private void initFields() {
      guid_ = "";
      label_ = "";
      latitude_ = 0D;
      longitude_ = 0D;
      altitude_ = 0D;
      createDate_ = 0L;
      scope_ = com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.getDefaultInstance();
      version_ = 0L;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (hasScope()) {
        if (!getScope().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (!extensionsAreInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      com.google.protobuf.GeneratedMessage
        .ExtendableMessage<com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage>.ExtensionWriter extensionWriter =
          newExtensionWriter();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getGuidBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getLabelBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeDouble(3, latitude_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeDouble(4, longitude_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeDouble(5, altitude_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeInt64(6, createDate_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeMessage(7, scope_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeInt64(8, version_);
      }
      extensionWriter.writeUntil(536870912, output);
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getGuidBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getLabelBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeDoubleSize(3, latitude_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeDoubleSize(4, longitude_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeDoubleSize(5, altitude_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(6, createDate_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(7, scope_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(8, version_);
      }
      size += extensionsSerializedSize();
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.ExtendableBuilder<
          com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage, Builder> implements com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.cloudlbs.platform.protocol.LocationProto.internal_static_location_FixedLocationMessage_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.cloudlbs.platform.protocol.LocationProto.internal_static_location_FixedLocationMessage_fieldAccessorTable;
      }
      
      // Construct using com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getScopeFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        guid_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        label_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        latitude_ = 0D;
        bitField0_ = (bitField0_ & ~0x00000004);
        longitude_ = 0D;
        bitField0_ = (bitField0_ & ~0x00000008);
        altitude_ = 0D;
        bitField0_ = (bitField0_ & ~0x00000010);
        createDate_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000020);
        if (scopeBuilder_ == null) {
          scope_ = com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.getDefaultInstance();
        } else {
          scopeBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000040);
        version_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000080);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage.getDescriptor();
      }
      
      public com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage getDefaultInstanceForType() {
        return com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage.getDefaultInstance();
      }
      
      public com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage build() {
        com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage buildPartial() {
        com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage result = new com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.guid_ = guid_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.label_ = label_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.latitude_ = latitude_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.longitude_ = longitude_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.altitude_ = altitude_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.createDate_ = createDate_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        if (scopeBuilder_ == null) {
          result.scope_ = scope_;
        } else {
          result.scope_ = scopeBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.version_ = version_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage) {
          return mergeFrom((com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage other) {
        if (other == com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage.getDefaultInstance()) return this;
        if (other.hasGuid()) {
          setGuid(other.getGuid());
        }
        if (other.hasLabel()) {
          setLabel(other.getLabel());
        }
        if (other.hasLatitude()) {
          setLatitude(other.getLatitude());
        }
        if (other.hasLongitude()) {
          setLongitude(other.getLongitude());
        }
        if (other.hasAltitude()) {
          setAltitude(other.getAltitude());
        }
        if (other.hasCreateDate()) {
          setCreateDate(other.getCreateDate());
        }
        if (other.hasScope()) {
          mergeScope(other.getScope());
        }
        if (other.hasVersion()) {
          setVersion(other.getVersion());
        }
        this.mergeExtensionFields(other);
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (hasScope()) {
          if (!getScope().isInitialized()) {
            
            return false;
          }
        }
        if (!extensionsAreInitialized()) {
          
          return false;
        }
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              guid_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              label_ = input.readBytes();
              break;
            }
            case 25: {
              bitField0_ |= 0x00000004;
              latitude_ = input.readDouble();
              break;
            }
            case 33: {
              bitField0_ |= 0x00000008;
              longitude_ = input.readDouble();
              break;
            }
            case 41: {
              bitField0_ |= 0x00000010;
              altitude_ = input.readDouble();
              break;
            }
            case 48: {
              bitField0_ |= 0x00000020;
              createDate_ = input.readInt64();
              break;
            }
            case 58: {
              com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.Builder subBuilder = com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.newBuilder();
              if (hasScope()) {
                subBuilder.mergeFrom(getScope());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setScope(subBuilder.buildPartial());
              break;
            }
            case 64: {
              bitField0_ |= 0x00000080;
              version_ = input.readInt64();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional string guid = 1;
      private java.lang.Object guid_ = "";
      public boolean hasGuid() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getGuid() {
        java.lang.Object ref = guid_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          guid_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setGuid(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        guid_ = value;
        onChanged();
        return this;
      }
      public Builder clearGuid() {
        bitField0_ = (bitField0_ & ~0x00000001);
        guid_ = getDefaultInstance().getGuid();
        onChanged();
        return this;
      }
      void setGuid(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        guid_ = value;
        onChanged();
      }
      
      // optional string label = 2;
      private java.lang.Object label_ = "";
      public boolean hasLabel() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getLabel() {
        java.lang.Object ref = label_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          label_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setLabel(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        label_ = value;
        onChanged();
        return this;
      }
      public Builder clearLabel() {
        bitField0_ = (bitField0_ & ~0x00000002);
        label_ = getDefaultInstance().getLabel();
        onChanged();
        return this;
      }
      void setLabel(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        label_ = value;
        onChanged();
      }
      
      // optional double latitude = 3;
      private double latitude_ ;
      public boolean hasLatitude() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public double getLatitude() {
        return latitude_;
      }
      public Builder setLatitude(double value) {
        bitField0_ |= 0x00000004;
        latitude_ = value;
        onChanged();
        return this;
      }
      public Builder clearLatitude() {
        bitField0_ = (bitField0_ & ~0x00000004);
        latitude_ = 0D;
        onChanged();
        return this;
      }
      
      // optional double longitude = 4;
      private double longitude_ ;
      public boolean hasLongitude() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public double getLongitude() {
        return longitude_;
      }
      public Builder setLongitude(double value) {
        bitField0_ |= 0x00000008;
        longitude_ = value;
        onChanged();
        return this;
      }
      public Builder clearLongitude() {
        bitField0_ = (bitField0_ & ~0x00000008);
        longitude_ = 0D;
        onChanged();
        return this;
      }
      
      // optional double altitude = 5;
      private double altitude_ ;
      public boolean hasAltitude() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public double getAltitude() {
        return altitude_;
      }
      public Builder setAltitude(double value) {
        bitField0_ |= 0x00000010;
        altitude_ = value;
        onChanged();
        return this;
      }
      public Builder clearAltitude() {
        bitField0_ = (bitField0_ & ~0x00000010);
        altitude_ = 0D;
        onChanged();
        return this;
      }
      
      // optional int64 createDate = 6;
      private long createDate_ ;
      public boolean hasCreateDate() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public long getCreateDate() {
        return createDate_;
      }
      public Builder setCreateDate(long value) {
        bitField0_ |= 0x00000020;
        createDate_ = value;
        onChanged();
        return this;
      }
      public Builder clearCreateDate() {
        bitField0_ = (bitField0_ & ~0x00000020);
        createDate_ = 0L;
        onChanged();
        return this;
      }
      
      // optional .scope.ScopeMessage scope = 7;
      private com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage scope_ = com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage, com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.Builder, com.cloudlbs.platform.protocol.ScopeProto.ScopeMessageOrBuilder> scopeBuilder_;
      public boolean hasScope() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      public com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage getScope() {
        if (scopeBuilder_ == null) {
          return scope_;
        } else {
          return scopeBuilder_.getMessage();
        }
      }
      public Builder setScope(com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage value) {
        if (scopeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          scope_ = value;
          onChanged();
        } else {
          scopeBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000040;
        return this;
      }
      public Builder setScope(
          com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.Builder builderForValue) {
        if (scopeBuilder_ == null) {
          scope_ = builderForValue.build();
          onChanged();
        } else {
          scopeBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000040;
        return this;
      }
      public Builder mergeScope(com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage value) {
        if (scopeBuilder_ == null) {
          if (((bitField0_ & 0x00000040) == 0x00000040) &&
              scope_ != com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.getDefaultInstance()) {
            scope_ =
              com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.newBuilder(scope_).mergeFrom(value).buildPartial();
          } else {
            scope_ = value;
          }
          onChanged();
        } else {
          scopeBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000040;
        return this;
      }
      public Builder clearScope() {
        if (scopeBuilder_ == null) {
          scope_ = com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.getDefaultInstance();
          onChanged();
        } else {
          scopeBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }
      public com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.Builder getScopeBuilder() {
        bitField0_ |= 0x00000040;
        onChanged();
        return getScopeFieldBuilder().getBuilder();
      }
      public com.cloudlbs.platform.protocol.ScopeProto.ScopeMessageOrBuilder getScopeOrBuilder() {
        if (scopeBuilder_ != null) {
          return scopeBuilder_.getMessageOrBuilder();
        } else {
          return scope_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage, com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.Builder, com.cloudlbs.platform.protocol.ScopeProto.ScopeMessageOrBuilder> 
          getScopeFieldBuilder() {
        if (scopeBuilder_ == null) {
          scopeBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage, com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage.Builder, com.cloudlbs.platform.protocol.ScopeProto.ScopeMessageOrBuilder>(
                  scope_,
                  getParentForChildren(),
                  isClean());
          scope_ = null;
        }
        return scopeBuilder_;
      }
      
      // optional int64 version = 8;
      private long version_ ;
      public boolean hasVersion() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      public long getVersion() {
        return version_;
      }
      public Builder setVersion(long value) {
        bitField0_ |= 0x00000080;
        version_ = value;
        onChanged();
        return this;
      }
      public Builder clearVersion() {
        bitField0_ = (bitField0_ & ~0x00000080);
        version_ = 0L;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:location.FixedLocationMessage)
    }
    
    static {
      defaultInstance = new FixedLocationMessage(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:location.FixedLocationMessage)
    public static final int ITEMS_FIELD_NUMBER = 1005;
    public static final
      com.google.protobuf.GeneratedMessage.GeneratedExtension<
        com.cloudlbs.platform.protocol.SearchProto.SearchResultMessage,
        java.util.List<com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage>> items = com.google.protobuf.GeneratedMessage
            .newMessageScopedGeneratedExtension(
          com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage.getDefaultInstance(),
          0,
          com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage.class,
          com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage.getDefaultInstance());
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_location_FixedLocationMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_location_FixedLocationMessage_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\016location.proto\022\010location\032\014search.proto" +
      "\032\013scope.proto\"\213\002\n\024FixedLocationMessage\022\014" +
      "\n\004guid\030\001 \001(\t\022\r\n\005label\030\002 \001(\t\022\020\n\010latitude\030" +
      "\003 \001(\001\022\021\n\tlongitude\030\004 \001(\001\022\020\n\010altitude\030\005 \001" +
      "(\001\022\022\n\ncreateDate\030\006 \001(\003\022\"\n\005scope\030\007 \001(\0132\023." +
      "scope.ScopeMessage\022\017\n\007version\030\010 \001(\003*\t\010\350\007" +
      "\020\200\200\200\200\0022K\n\005items\022\033.search.SearchResultMes" +
      "sage\030\355\007 \003(\0132\036.location.FixedLocationMess" +
      "ageB/\n\036com.cloudlbs.platform.protocolB\rL" +
      "ocationProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_location_FixedLocationMessage_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_location_FixedLocationMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_location_FixedLocationMessage_descriptor,
              new java.lang.String[] { "Guid", "Label", "Latitude", "Longitude", "Altitude", "CreateDate", "Scope", "Version", },
              com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage.class,
              com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.cloudlbs.platform.protocol.SearchProto.getDescriptor(),
          com.cloudlbs.platform.protocol.ScopeProto.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
