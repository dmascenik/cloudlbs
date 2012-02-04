// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: system.proto

package com.cloudlbs.platform.protocol;

public final class SystemProto {
  private SystemProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registry.add(com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage.items);
  }
  public interface SystemPropertyMessageOrBuilder extends
      com.google.protobuf.GeneratedMessage.
          ExtendableMessageOrBuilder<SystemPropertyMessage> {
    
    // optional string guid = 1;
    boolean hasGuid();
    String getGuid();
    
    // optional int64 version = 2;
    boolean hasVersion();
    long getVersion();
    
    // optional int64 createDate = 3;
    boolean hasCreateDate();
    long getCreateDate();
    
    // optional string key = 4;
    boolean hasKey();
    String getKey();
    
    // optional string prettyName = 5;
    boolean hasPrettyName();
    String getPrettyName();
    
    // optional string description = 6;
    boolean hasDescription();
    String getDescription();
    
    // optional string value = 7;
    boolean hasValue();
    String getValue();
    
    // optional string category = 8;
    boolean hasCategory();
    String getCategory();
  }
  public static final class SystemPropertyMessage extends
      com.google.protobuf.GeneratedMessage.ExtendableMessage<
        SystemPropertyMessage> implements SystemPropertyMessageOrBuilder {
    // Use SystemPropertyMessage.newBuilder() to construct.
    private SystemPropertyMessage(Builder builder) {
      super(builder);
    }
    private SystemPropertyMessage(boolean noInit) {}
    
    private static final SystemPropertyMessage defaultInstance;
    public static SystemPropertyMessage getDefaultInstance() {
      return defaultInstance;
    }
    
    public SystemPropertyMessage getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.cloudlbs.platform.protocol.SystemProto.internal_static_system_SystemPropertyMessage_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.cloudlbs.platform.protocol.SystemProto.internal_static_system_SystemPropertyMessage_fieldAccessorTable;
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
    
    // optional int64 version = 2;
    public static final int VERSION_FIELD_NUMBER = 2;
    private long version_;
    public boolean hasVersion() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public long getVersion() {
      return version_;
    }
    
    // optional int64 createDate = 3;
    public static final int CREATEDATE_FIELD_NUMBER = 3;
    private long createDate_;
    public boolean hasCreateDate() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public long getCreateDate() {
      return createDate_;
    }
    
    // optional string key = 4;
    public static final int KEY_FIELD_NUMBER = 4;
    private java.lang.Object key_;
    public boolean hasKey() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public String getKey() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          key_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string prettyName = 5;
    public static final int PRETTYNAME_FIELD_NUMBER = 5;
    private java.lang.Object prettyName_;
    public boolean hasPrettyName() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public String getPrettyName() {
      java.lang.Object ref = prettyName_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          prettyName_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getPrettyNameBytes() {
      java.lang.Object ref = prettyName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        prettyName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string description = 6;
    public static final int DESCRIPTION_FIELD_NUMBER = 6;
    private java.lang.Object description_;
    public boolean hasDescription() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public String getDescription() {
      java.lang.Object ref = description_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          description_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getDescriptionBytes() {
      java.lang.Object ref = description_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        description_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string value = 7;
    public static final int VALUE_FIELD_NUMBER = 7;
    private java.lang.Object value_;
    public boolean hasValue() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    public String getValue() {
      java.lang.Object ref = value_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          value_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getValueBytes() {
      java.lang.Object ref = value_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        value_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string category = 8;
    public static final int CATEGORY_FIELD_NUMBER = 8;
    private java.lang.Object category_;
    public boolean hasCategory() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    public String getCategory() {
      java.lang.Object ref = category_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          category_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getCategoryBytes() {
      java.lang.Object ref = category_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        category_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    private void initFields() {
      guid_ = "";
      version_ = 0L;
      createDate_ = 0L;
      key_ = "";
      prettyName_ = "";
      description_ = "";
      value_ = "";
      category_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
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
        .ExtendableMessage<com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage>.ExtensionWriter extensionWriter =
          newExtensionWriter();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getGuidBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt64(2, version_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt64(3, createDate_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, getKeyBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, getPrettyNameBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(6, getDescriptionBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBytes(7, getValueBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeBytes(8, getCategoryBytes());
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
          .computeInt64Size(2, version_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(3, createDate_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getKeyBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getPrettyNameBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, getDescriptionBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, getValueBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(8, getCategoryBytes());
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
    
    public static com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage parseDelimitedFrom(
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
    public static com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage prototype) {
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
          com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage, Builder> implements com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.cloudlbs.platform.protocol.SystemProto.internal_static_system_SystemPropertyMessage_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.cloudlbs.platform.protocol.SystemProto.internal_static_system_SystemPropertyMessage_fieldAccessorTable;
      }
      
      // Construct using com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        guid_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        version_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        createDate_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000004);
        key_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        prettyName_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        description_ = "";
        bitField0_ = (bitField0_ & ~0x00000020);
        value_ = "";
        bitField0_ = (bitField0_ & ~0x00000040);
        category_ = "";
        bitField0_ = (bitField0_ & ~0x00000080);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage.getDescriptor();
      }
      
      public com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage getDefaultInstanceForType() {
        return com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage.getDefaultInstance();
      }
      
      public com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage build() {
        com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage buildPartial() {
        com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage result = new com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.guid_ = guid_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.version_ = version_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.createDate_ = createDate_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.key_ = key_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.prettyName_ = prettyName_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.description_ = description_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.value_ = value_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.category_ = category_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage) {
          return mergeFrom((com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage other) {
        if (other == com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage.getDefaultInstance()) return this;
        if (other.hasGuid()) {
          setGuid(other.getGuid());
        }
        if (other.hasVersion()) {
          setVersion(other.getVersion());
        }
        if (other.hasCreateDate()) {
          setCreateDate(other.getCreateDate());
        }
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasPrettyName()) {
          setPrettyName(other.getPrettyName());
        }
        if (other.hasDescription()) {
          setDescription(other.getDescription());
        }
        if (other.hasValue()) {
          setValue(other.getValue());
        }
        if (other.hasCategory()) {
          setCategory(other.getCategory());
        }
        this.mergeExtensionFields(other);
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
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
            case 16: {
              bitField0_ |= 0x00000002;
              version_ = input.readInt64();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              createDate_ = input.readInt64();
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              key_ = input.readBytes();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              prettyName_ = input.readBytes();
              break;
            }
            case 50: {
              bitField0_ |= 0x00000020;
              description_ = input.readBytes();
              break;
            }
            case 58: {
              bitField0_ |= 0x00000040;
              value_ = input.readBytes();
              break;
            }
            case 66: {
              bitField0_ |= 0x00000080;
              category_ = input.readBytes();
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
      
      // optional int64 version = 2;
      private long version_ ;
      public boolean hasVersion() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public long getVersion() {
        return version_;
      }
      public Builder setVersion(long value) {
        bitField0_ |= 0x00000002;
        version_ = value;
        onChanged();
        return this;
      }
      public Builder clearVersion() {
        bitField0_ = (bitField0_ & ~0x00000002);
        version_ = 0L;
        onChanged();
        return this;
      }
      
      // optional int64 createDate = 3;
      private long createDate_ ;
      public boolean hasCreateDate() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public long getCreateDate() {
        return createDate_;
      }
      public Builder setCreateDate(long value) {
        bitField0_ |= 0x00000004;
        createDate_ = value;
        onChanged();
        return this;
      }
      public Builder clearCreateDate() {
        bitField0_ = (bitField0_ & ~0x00000004);
        createDate_ = 0L;
        onChanged();
        return this;
      }
      
      // optional string key = 4;
      private java.lang.Object key_ = "";
      public boolean hasKey() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getKey() {
        java.lang.Object ref = key_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          key_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setKey(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        key_ = value;
        onChanged();
        return this;
      }
      public Builder clearKey() {
        bitField0_ = (bitField0_ & ~0x00000008);
        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }
      void setKey(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        key_ = value;
        onChanged();
      }
      
      // optional string prettyName = 5;
      private java.lang.Object prettyName_ = "";
      public boolean hasPrettyName() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public String getPrettyName() {
        java.lang.Object ref = prettyName_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          prettyName_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setPrettyName(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        prettyName_ = value;
        onChanged();
        return this;
      }
      public Builder clearPrettyName() {
        bitField0_ = (bitField0_ & ~0x00000010);
        prettyName_ = getDefaultInstance().getPrettyName();
        onChanged();
        return this;
      }
      void setPrettyName(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000010;
        prettyName_ = value;
        onChanged();
      }
      
      // optional string description = 6;
      private java.lang.Object description_ = "";
      public boolean hasDescription() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public String getDescription() {
        java.lang.Object ref = description_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          description_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setDescription(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        description_ = value;
        onChanged();
        return this;
      }
      public Builder clearDescription() {
        bitField0_ = (bitField0_ & ~0x00000020);
        description_ = getDefaultInstance().getDescription();
        onChanged();
        return this;
      }
      void setDescription(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000020;
        description_ = value;
        onChanged();
      }
      
      // optional string value = 7;
      private java.lang.Object value_ = "";
      public boolean hasValue() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      public String getValue() {
        java.lang.Object ref = value_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          value_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setValue(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        value_ = value;
        onChanged();
        return this;
      }
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000040);
        value_ = getDefaultInstance().getValue();
        onChanged();
        return this;
      }
      void setValue(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000040;
        value_ = value;
        onChanged();
      }
      
      // optional string category = 8;
      private java.lang.Object category_ = "";
      public boolean hasCategory() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      public String getCategory() {
        java.lang.Object ref = category_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          category_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setCategory(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000080;
        category_ = value;
        onChanged();
        return this;
      }
      public Builder clearCategory() {
        bitField0_ = (bitField0_ & ~0x00000080);
        category_ = getDefaultInstance().getCategory();
        onChanged();
        return this;
      }
      void setCategory(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000080;
        category_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:system.SystemPropertyMessage)
    }
    
    static {
      defaultInstance = new SystemPropertyMessage(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:system.SystemPropertyMessage)
    public static final int ITEMS_FIELD_NUMBER = 1007;
    public static final
      com.google.protobuf.GeneratedMessage.GeneratedExtension<
        com.cloudlbs.platform.protocol.SearchProto.SearchResultMessage,
        java.util.List<com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage>> items = com.google.protobuf.GeneratedMessage
            .newMessageScopedGeneratedExtension(
          com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage.getDefaultInstance(),
          0,
          com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage.class,
          com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage.getDefaultInstance());
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_system_SystemPropertyMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_system_SystemPropertyMessage_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014system.proto\022\006system\032\014search.proto\"\370\001\n" +
      "\025SystemPropertyMessage\022\014\n\004guid\030\001 \001(\t\022\017\n\007" +
      "version\030\002 \001(\003\022\022\n\ncreateDate\030\003 \001(\003\022\013\n\003key" +
      "\030\004 \001(\t\022\022\n\nprettyName\030\005 \001(\t\022\023\n\013descriptio" +
      "n\030\006 \001(\t\022\r\n\005value\030\007 \001(\t\022\020\n\010category\030\010 \001(\t" +
      "*\t\010\350\007\020\200\200\200\200\0022J\n\005items\022\033.search.SearchResu" +
      "ltMessage\030\357\007 \003(\0132\035.system.SystemProperty" +
      "MessageB-\n\036com.cloudlbs.platform.protoco" +
      "lB\013SystemProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_system_SystemPropertyMessage_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_system_SystemPropertyMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_system_SystemPropertyMessage_descriptor,
              new java.lang.String[] { "Guid", "Version", "CreateDate", "Key", "PrettyName", "Description", "Value", "Category", },
              com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage.class,
              com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.cloudlbs.platform.protocol.SearchProto.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}