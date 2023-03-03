#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_zerodev_wa_utils_Secured_getDirImages(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF("/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/Sent/");
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_zerodev_wa_utils_Secured_getDirVideos(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF("/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video/Sent/");
}