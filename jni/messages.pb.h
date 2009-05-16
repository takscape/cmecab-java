// Generated by the protocol buffer compiler.  DO NOT EDIT!

#ifndef PROTOBUF_messages_2eproto__INCLUDED
#define PROTOBUF_messages_2eproto__INCLUDED

#include <string>

#include <google/protobuf/stubs/common.h>

#if GOOGLE_PROTOBUF_VERSION < 2001000
#error This file was generated by a newer version of protoc which is
#error incompatible with your Protocol Buffer headers.  Please update
#error your headers.
#endif
#if 2001000 < GOOGLE_PROTOBUF_MIN_PROTOC_VERSION
#error This file was generated by an older version of protoc which is
#error incompatible with your Protocol Buffer headers.  Please
#error regenerate this file with a newer version of protoc.
#endif

#include <google/protobuf/generated_message_reflection.h>
#include <google/protobuf/repeated_field.h>
#include <google/protobuf/extension_set.h>

namespace net {
namespace moraleboost {
namespace mecab {
namespace impl {

// Internal implementation detail -- do not call these.
void  protobuf_AddDesc_messages_2eproto();
void protobuf_AssignDesc_messages_2eproto();
void protobuf_ShutdownFile_messages_2eproto();

class ParsingRequest;
class ParsingResponse;
class ParsingResponse_Morpheme;

// ===================================================================

class ParsingRequest : public ::google::protobuf::Message {
 public:
  ParsingRequest();
  virtual ~ParsingRequest();
  
  ParsingRequest(const ParsingRequest& from);
  
  inline ParsingRequest& operator=(const ParsingRequest& from) {
    CopyFrom(from);
    return *this;
  }
  
  inline const ::google::protobuf::UnknownFieldSet& unknown_fields() const {
    return _unknown_fields_;
  }
  
  inline ::google::protobuf::UnknownFieldSet* mutable_unknown_fields() {
    return &_unknown_fields_;
  }
  
  static const ::google::protobuf::Descriptor* descriptor();
  static const ParsingRequest& default_instance();
  void Swap(ParsingRequest* other);
  
  // implements Message ----------------------------------------------
  
  ParsingRequest* New() const;
  void CopyFrom(const ::google::protobuf::Message& from);
  void MergeFrom(const ::google::protobuf::Message& from);
  void CopyFrom(const ParsingRequest& from);
  void MergeFrom(const ParsingRequest& from);
  void Clear();
  bool IsInitialized() const;
  
  int ByteSize() const;
  bool MergePartialFromCodedStream(
      ::google::protobuf::io::CodedInputStream* input);
  void SerializeWithCachedSizes(
      ::google::protobuf::io::CodedOutputStream* output) const;
  ::google::protobuf::uint8* SerializeWithCachedSizesToArray(::google::protobuf::uint8* output) const;
  int GetCachedSize() const { return _cached_size_; }
  private:
  void SharedCtor();
  void SharedDtor();
  void SetCachedSize(int size) const { _cached_size_ = size; }
  public:
  
  const ::google::protobuf::Descriptor* GetDescriptor() const;
  const ::google::protobuf::Reflection* GetReflection() const;
  
  // nested types ----------------------------------------------------
  
  // accessors -------------------------------------------------------
  
  // required string text = 1;
  inline bool has_text() const;
  inline void clear_text();
  static const int kTextFieldNumber = 1;
  inline const ::std::string& text() const;
  inline void set_text(const ::std::string& value);
  inline void set_text(const char* value);
  inline void set_text(const char* value, size_t size);
  inline ::std::string* mutable_text();
  
 private:
  ::google::protobuf::UnknownFieldSet _unknown_fields_;
  mutable int _cached_size_;
  
  ::std::string* text_;
  static const ::std::string _default_text_;
  friend void  protobuf_AddDesc_messages_2eproto();
  friend void protobuf_AssignDesc_messages_2eproto();
  friend void protobuf_ShutdownFile_messages_2eproto();
  ::google::protobuf::uint32 _has_bits_[(1 + 31) / 32];
  
  // WHY DOES & HAVE LOWER PRECEDENCE THAN != !?
  inline bool _has_bit(int index) const {
    return (_has_bits_[index / 32] & (1u << (index % 32))) != 0;
  }
  inline void _set_bit(int index) {
    _has_bits_[index / 32] |= (1u << (index % 32));
  }
  inline void _clear_bit(int index) {
    _has_bits_[index / 32] &= ~(1u << (index % 32));
  }
  
  void InitAsDefaultInstance();
  static ParsingRequest* default_instance_;
};
// -------------------------------------------------------------------

class ParsingResponse_Morpheme : public ::google::protobuf::Message {
 public:
  ParsingResponse_Morpheme();
  virtual ~ParsingResponse_Morpheme();
  
  ParsingResponse_Morpheme(const ParsingResponse_Morpheme& from);
  
  inline ParsingResponse_Morpheme& operator=(const ParsingResponse_Morpheme& from) {
    CopyFrom(from);
    return *this;
  }
  
  inline const ::google::protobuf::UnknownFieldSet& unknown_fields() const {
    return _unknown_fields_;
  }
  
  inline ::google::protobuf::UnknownFieldSet* mutable_unknown_fields() {
    return &_unknown_fields_;
  }
  
  static const ::google::protobuf::Descriptor* descriptor();
  static const ParsingResponse_Morpheme& default_instance();
  void Swap(ParsingResponse_Morpheme* other);
  
  // implements Message ----------------------------------------------
  
  ParsingResponse_Morpheme* New() const;
  void CopyFrom(const ::google::protobuf::Message& from);
  void MergeFrom(const ::google::protobuf::Message& from);
  void CopyFrom(const ParsingResponse_Morpheme& from);
  void MergeFrom(const ParsingResponse_Morpheme& from);
  void Clear();
  bool IsInitialized() const;
  
  int ByteSize() const;
  bool MergePartialFromCodedStream(
      ::google::protobuf::io::CodedInputStream* input);
  void SerializeWithCachedSizes(
      ::google::protobuf::io::CodedOutputStream* output) const;
  ::google::protobuf::uint8* SerializeWithCachedSizesToArray(::google::protobuf::uint8* output) const;
  int GetCachedSize() const { return _cached_size_; }
  private:
  void SharedCtor();
  void SharedDtor();
  void SetCachedSize(int size) const { _cached_size_ = size; }
  public:
  
  const ::google::protobuf::Descriptor* GetDescriptor() const;
  const ::google::protobuf::Reflection* GetReflection() const;
  
  // nested types ----------------------------------------------------
  
  // accessors -------------------------------------------------------
  
  // required string surface = 1;
  inline bool has_surface() const;
  inline void clear_surface();
  static const int kSurfaceFieldNumber = 1;
  inline const ::std::string& surface() const;
  inline void set_surface(const ::std::string& value);
  inline void set_surface(const char* value);
  inline void set_surface(const char* value, size_t size);
  inline ::std::string* mutable_surface();
  
  // optional string blank = 2;
  inline bool has_blank() const;
  inline void clear_blank();
  static const int kBlankFieldNumber = 2;
  inline const ::std::string& blank() const;
  inline void set_blank(const ::std::string& value);
  inline void set_blank(const char* value);
  inline void set_blank(const char* value, size_t size);
  inline ::std::string* mutable_blank();
  
  // required string feature = 3;
  inline bool has_feature() const;
  inline void clear_feature();
  static const int kFeatureFieldNumber = 3;
  inline const ::std::string& feature() const;
  inline void set_feature(const ::std::string& value);
  inline void set_feature(const char* value);
  inline void set_feature(const char* value, size_t size);
  inline ::std::string* mutable_feature();
  
  // required uint32 posid = 4;
  inline bool has_posid() const;
  inline void clear_posid();
  static const int kPosidFieldNumber = 4;
  inline ::google::protobuf::uint32 posid() const;
  inline void set_posid(::google::protobuf::uint32 value);
  
 private:
  ::google::protobuf::UnknownFieldSet _unknown_fields_;
  mutable int _cached_size_;
  
  ::std::string* surface_;
  static const ::std::string _default_surface_;
  ::std::string* blank_;
  static const ::std::string _default_blank_;
  ::std::string* feature_;
  static const ::std::string _default_feature_;
  ::google::protobuf::uint32 posid_;
  friend void  protobuf_AddDesc_messages_2eproto();
  friend void protobuf_AssignDesc_messages_2eproto();
  friend void protobuf_ShutdownFile_messages_2eproto();
  ::google::protobuf::uint32 _has_bits_[(4 + 31) / 32];
  
  // WHY DOES & HAVE LOWER PRECEDENCE THAN != !?
  inline bool _has_bit(int index) const {
    return (_has_bits_[index / 32] & (1u << (index % 32))) != 0;
  }
  inline void _set_bit(int index) {
    _has_bits_[index / 32] |= (1u << (index % 32));
  }
  inline void _clear_bit(int index) {
    _has_bits_[index / 32] &= ~(1u << (index % 32));
  }
  
  void InitAsDefaultInstance();
  static ParsingResponse_Morpheme* default_instance_;
};
// -------------------------------------------------------------------

class ParsingResponse : public ::google::protobuf::Message {
 public:
  ParsingResponse();
  virtual ~ParsingResponse();
  
  ParsingResponse(const ParsingResponse& from);
  
  inline ParsingResponse& operator=(const ParsingResponse& from) {
    CopyFrom(from);
    return *this;
  }
  
  inline const ::google::protobuf::UnknownFieldSet& unknown_fields() const {
    return _unknown_fields_;
  }
  
  inline ::google::protobuf::UnknownFieldSet* mutable_unknown_fields() {
    return &_unknown_fields_;
  }
  
  static const ::google::protobuf::Descriptor* descriptor();
  static const ParsingResponse& default_instance();
  void Swap(ParsingResponse* other);
  
  // implements Message ----------------------------------------------
  
  ParsingResponse* New() const;
  void CopyFrom(const ::google::protobuf::Message& from);
  void MergeFrom(const ::google::protobuf::Message& from);
  void CopyFrom(const ParsingResponse& from);
  void MergeFrom(const ParsingResponse& from);
  void Clear();
  bool IsInitialized() const;
  
  int ByteSize() const;
  bool MergePartialFromCodedStream(
      ::google::protobuf::io::CodedInputStream* input);
  void SerializeWithCachedSizes(
      ::google::protobuf::io::CodedOutputStream* output) const;
  ::google::protobuf::uint8* SerializeWithCachedSizesToArray(::google::protobuf::uint8* output) const;
  int GetCachedSize() const { return _cached_size_; }
  private:
  void SharedCtor();
  void SharedDtor();
  void SetCachedSize(int size) const { _cached_size_ = size; }
  public:
  
  const ::google::protobuf::Descriptor* GetDescriptor() const;
  const ::google::protobuf::Reflection* GetReflection() const;
  
  // nested types ----------------------------------------------------
  
  typedef ParsingResponse_Morpheme Morpheme;
  
  // accessors -------------------------------------------------------
  
  // repeated .net.moraleboost.mecab.impl.ParsingResponse.Morpheme morpheme = 1;
  inline int morpheme_size() const;
  inline void clear_morpheme();
  static const int kMorphemeFieldNumber = 1;
  inline const ::google::protobuf::RepeatedPtrField< ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme >& morpheme() const;
  inline ::google::protobuf::RepeatedPtrField< ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme >* mutable_morpheme();
  inline const ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme& morpheme(int index) const;
  inline ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme* mutable_morpheme(int index);
  inline ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme* add_morpheme();
  
 private:
  ::google::protobuf::UnknownFieldSet _unknown_fields_;
  mutable int _cached_size_;
  
  ::google::protobuf::RepeatedPtrField< ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme > morpheme_;
  friend void  protobuf_AddDesc_messages_2eproto();
  friend void protobuf_AssignDesc_messages_2eproto();
  friend void protobuf_ShutdownFile_messages_2eproto();
  ::google::protobuf::uint32 _has_bits_[(1 + 31) / 32];
  
  // WHY DOES & HAVE LOWER PRECEDENCE THAN != !?
  inline bool _has_bit(int index) const {
    return (_has_bits_[index / 32] & (1u << (index % 32))) != 0;
  }
  inline void _set_bit(int index) {
    _has_bits_[index / 32] |= (1u << (index % 32));
  }
  inline void _clear_bit(int index) {
    _has_bits_[index / 32] &= ~(1u << (index % 32));
  }
  
  void InitAsDefaultInstance();
  static ParsingResponse* default_instance_;
};
// ===================================================================


// ===================================================================


// ===================================================================

// ParsingRequest

// required string text = 1;
inline bool ParsingRequest::has_text() const {
  return _has_bit(0);
}
inline void ParsingRequest::clear_text() {
  if (text_ != &_default_text_) {
    text_->clear();
  }
  _clear_bit(0);
}
inline const ::std::string& ParsingRequest::text() const {
  return *text_;
}
inline void ParsingRequest::set_text(const ::std::string& value) {
  _set_bit(0);
  if (text_ == &_default_text_) {
    text_ = new ::std::string;
  }
  text_->assign(value);
}
inline void ParsingRequest::set_text(const char* value) {
  _set_bit(0);
  if (text_ == &_default_text_) {
    text_ = new ::std::string;
  }
  text_->assign(value);
}
inline void ParsingRequest::set_text(const char* value, size_t size) {
  _set_bit(0);
  if (text_ == &_default_text_) {
    text_ = new ::std::string;
  }
  text_->assign(reinterpret_cast<const char*>(value), size);
}
inline ::std::string* ParsingRequest::mutable_text() {
  _set_bit(0);
  if (text_ == &_default_text_) {
    text_ = new ::std::string;
  }
  return text_;
}

// -------------------------------------------------------------------

// ParsingResponse_Morpheme

// required string surface = 1;
inline bool ParsingResponse_Morpheme::has_surface() const {
  return _has_bit(0);
}
inline void ParsingResponse_Morpheme::clear_surface() {
  if (surface_ != &_default_surface_) {
    surface_->clear();
  }
  _clear_bit(0);
}
inline const ::std::string& ParsingResponse_Morpheme::surface() const {
  return *surface_;
}
inline void ParsingResponse_Morpheme::set_surface(const ::std::string& value) {
  _set_bit(0);
  if (surface_ == &_default_surface_) {
    surface_ = new ::std::string;
  }
  surface_->assign(value);
}
inline void ParsingResponse_Morpheme::set_surface(const char* value) {
  _set_bit(0);
  if (surface_ == &_default_surface_) {
    surface_ = new ::std::string;
  }
  surface_->assign(value);
}
inline void ParsingResponse_Morpheme::set_surface(const char* value, size_t size) {
  _set_bit(0);
  if (surface_ == &_default_surface_) {
    surface_ = new ::std::string;
  }
  surface_->assign(reinterpret_cast<const char*>(value), size);
}
inline ::std::string* ParsingResponse_Morpheme::mutable_surface() {
  _set_bit(0);
  if (surface_ == &_default_surface_) {
    surface_ = new ::std::string;
  }
  return surface_;
}

// optional string blank = 2;
inline bool ParsingResponse_Morpheme::has_blank() const {
  return _has_bit(1);
}
inline void ParsingResponse_Morpheme::clear_blank() {
  if (blank_ != &_default_blank_) {
    blank_->clear();
  }
  _clear_bit(1);
}
inline const ::std::string& ParsingResponse_Morpheme::blank() const {
  return *blank_;
}
inline void ParsingResponse_Morpheme::set_blank(const ::std::string& value) {
  _set_bit(1);
  if (blank_ == &_default_blank_) {
    blank_ = new ::std::string;
  }
  blank_->assign(value);
}
inline void ParsingResponse_Morpheme::set_blank(const char* value) {
  _set_bit(1);
  if (blank_ == &_default_blank_) {
    blank_ = new ::std::string;
  }
  blank_->assign(value);
}
inline void ParsingResponse_Morpheme::set_blank(const char* value, size_t size) {
  _set_bit(1);
  if (blank_ == &_default_blank_) {
    blank_ = new ::std::string;
  }
  blank_->assign(reinterpret_cast<const char*>(value), size);
}
inline ::std::string* ParsingResponse_Morpheme::mutable_blank() {
  _set_bit(1);
  if (blank_ == &_default_blank_) {
    blank_ = new ::std::string;
  }
  return blank_;
}

// required string feature = 3;
inline bool ParsingResponse_Morpheme::has_feature() const {
  return _has_bit(2);
}
inline void ParsingResponse_Morpheme::clear_feature() {
  if (feature_ != &_default_feature_) {
    feature_->clear();
  }
  _clear_bit(2);
}
inline const ::std::string& ParsingResponse_Morpheme::feature() const {
  return *feature_;
}
inline void ParsingResponse_Morpheme::set_feature(const ::std::string& value) {
  _set_bit(2);
  if (feature_ == &_default_feature_) {
    feature_ = new ::std::string;
  }
  feature_->assign(value);
}
inline void ParsingResponse_Morpheme::set_feature(const char* value) {
  _set_bit(2);
  if (feature_ == &_default_feature_) {
    feature_ = new ::std::string;
  }
  feature_->assign(value);
}
inline void ParsingResponse_Morpheme::set_feature(const char* value, size_t size) {
  _set_bit(2);
  if (feature_ == &_default_feature_) {
    feature_ = new ::std::string;
  }
  feature_->assign(reinterpret_cast<const char*>(value), size);
}
inline ::std::string* ParsingResponse_Morpheme::mutable_feature() {
  _set_bit(2);
  if (feature_ == &_default_feature_) {
    feature_ = new ::std::string;
  }
  return feature_;
}

// required uint32 posid = 4;
inline bool ParsingResponse_Morpheme::has_posid() const {
  return _has_bit(3);
}
inline void ParsingResponse_Morpheme::clear_posid() {
  posid_ = 0u;
  _clear_bit(3);
}
inline ::google::protobuf::uint32 ParsingResponse_Morpheme::posid() const {
  return posid_;
}
inline void ParsingResponse_Morpheme::set_posid(::google::protobuf::uint32 value) {
  _set_bit(3);
  posid_ = value;
}

// -------------------------------------------------------------------

// ParsingResponse

// repeated .net.moraleboost.mecab.impl.ParsingResponse.Morpheme morpheme = 1;
inline int ParsingResponse::morpheme_size() const {
  return morpheme_.size();
}
inline void ParsingResponse::clear_morpheme() {
  morpheme_.Clear();
}
inline const ::google::protobuf::RepeatedPtrField< ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme >&
ParsingResponse::morpheme() const {
  return morpheme_;
}
inline ::google::protobuf::RepeatedPtrField< ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme >*
ParsingResponse::mutable_morpheme() {
  return &morpheme_;
}
inline const ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme& ParsingResponse::morpheme(int index) const {
  return morpheme_.Get(index);
}
inline ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme* ParsingResponse::mutable_morpheme(int index) {
  return morpheme_.Mutable(index);
}
inline ::net::moraleboost::mecab::impl::ParsingResponse_Morpheme* ParsingResponse::add_morpheme() {
  return morpheme_.Add();
}


}  // namespace impl
}  // namespace mecab
}  // namespace moraleboost
}  // namespace net
#endif  // PROTOBUF_messages_2eproto__INCLUDED
