//package com.rittmann.leagueoflegendschamps.data.network
//
//class NetworkCall<T> {
//
//    lateinit var call: Call<T>
//
//    fun makeCall(
//        call: Call<T>,
//        functions: GmsProviderSuccessFactory<T>
//    ) {
//
//        this.call = call
//        val callBackKt =
//            CallBackKt(
//                functions
//            )
//        this.call.clone().enqueue(callBackKt)
//    }
//
//    class CallBackKt<T>(functions: GmsProviderSuccessFactory<T>) : Callback<T> {
//
//        private var mFunctions: GmsProviderSuccessFactory<T> = functions
//
//        override fun onFailure(call: Call<T>, t: Throwable) {
//            mFunctions.mConnectionFailure()
//        }
//
//        override fun onResponse(call: Call<T>, response: Response<T>) {
//            when {
//                response.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> return
//                response.raw().isSuccessful -> {
//                    when (mFunctions.mTypeSuccess) {
//                        GmsProviderSuccessFactory.TypeSuccess.VOID -> mFunctions.mSuccessVoid()
//                        GmsProviderSuccessFactory.TypeSuccess.WITH_RETURN -> response.body()?.let {
//                            mFunctions.mSuccess(
//                                it
//                            )
//                        }
//                        else -> return
//                    }
//                }
//                else -> try {
//                    val jObjError = JSONObject(response.errorBody()?.string())
//                    mFunctions.mFailure(ErrorAPI.setErrorExtra(jObjError))
//                } catch (e: Exception) {
//                    mFunctions.mGenericFailure()
//                }
//            }
//        }
//    }
//
//    fun cancel() {
//        if (::call.isInitialized) {
//            call.cancel()
//        }
//    }
//}