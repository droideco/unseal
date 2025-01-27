#ifdef __cplusplus
extern "C" {
#endif

#include <jni.h>

static jobject handle;
static jmethodID setHiddenApiExemptions_methodID;

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, __attribute__((unused)) void *reserved) {
    JNIEnv *env;
    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6) != JNI_OK) return JNI_ERR;
    jclass clazz = (*env)->FindClass(env, "dalvik/system/VMRuntime");
    if (clazz == NULL) return JNI_ERR;
    jmethodID getRuntime_MethodID = (*env)->GetStaticMethodID(env, clazz, "getRuntime", "()Ldalvik/system/VMRuntime;");
    if (getRuntime_MethodID == NULL) return JNI_ERR;
    setHiddenApiExemptions_methodID = (*env)->GetStaticMethodID(env, clazz, "setHiddenApiExemptions", "([Ljava/lang/String;)V");
    if (setHiddenApiExemptions_methodID == NULL) return JNI_ERR;
    handle = (*env)->CallStaticObjectMethod(env, clazz, getRuntime_MethodID);
    if (handle == NULL) return JNI_ERR;
    handle = (*env)->NewGlobalRef(env, handle);
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL
JNI_OnUnload(JavaVM* vm, __attribute__((unused)) void* reserved) {
    JNIEnv *env;
    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6) != JNI_OK) return;
    (*env)->DeleteGlobalRef(env, handle);
    handle = NULL;
    setHiddenApiExemptions_methodID = NULL;
}

JNIEXPORT void JNICALL
Java_io_github_droideco_unseal_Unseal_setHiddenAPIExemptions(JNIEnv *env, __attribute__((unused)) jclass clazz,
                                                             jobjectArray signature_prefixes) {
    (*env)->CallVoidMethod(env, handle, setHiddenApiExemptions_methodID, signature_prefixes);
}

#ifdef __cplusplus
}
#endif
