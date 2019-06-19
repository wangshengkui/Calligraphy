/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\GITHUB\\Calligraphy\\src\\com\\jinke\\rloginservice\\IReadingsLoginService.aidl
 */
package com.jinke.rloginservice;
public interface IReadingsLoginService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.jinke.rloginservice.IReadingsLoginService
{
private static final java.lang.String DESCRIPTOR = "com.jinke.rloginservice.IReadingsLoginService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.jinke.rloginservice.IReadingsLoginService interface,
 * generating a proxy if needed.
 */
public static com.jinke.rloginservice.IReadingsLoginService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.jinke.rloginservice.IReadingsLoginService))) {
return ((com.jinke.rloginservice.IReadingsLoginService)iin);
}
return new com.jinke.rloginservice.IReadingsLoginService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_isLogin:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isLogin();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isBinding:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isBinding();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_login:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
boolean _result = this.login(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_unBinding:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.unBinding();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getSimID:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getSimID();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getUserInfo:
{
data.enforceInterface(DESCRIPTOR);
UserInfo _result = this.getUserInfo();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_loginActivity:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.loginActivity();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.jinke.rloginservice.IReadingsLoginService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public boolean isLogin() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isLogin, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isBinding() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isBinding, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean login(java.lang.String username, java.lang.String password) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(username);
_data.writeString(password);
mRemote.transact(Stub.TRANSACTION_login, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean unBinding() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_unBinding, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getSimID() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSimID, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public UserInfo getUserInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
UserInfo _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUserInfo, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = UserInfo.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean loginActivity() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_loginActivity, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_isLogin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_isBinding = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_login = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_unBinding = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getSimID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getUserInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_loginActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
public boolean isLogin() throws android.os.RemoteException;
public boolean isBinding() throws android.os.RemoteException;
public boolean login(java.lang.String username, java.lang.String password) throws android.os.RemoteException;
public boolean unBinding() throws android.os.RemoteException;
public java.lang.String getSimID() throws android.os.RemoteException;
public UserInfo getUserInfo() throws android.os.RemoteException;
public boolean loginActivity() throws android.os.RemoteException;
}
