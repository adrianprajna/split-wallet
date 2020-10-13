package edu.bluejack20_1.splitwallet.support_class

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.text.TextUtils
import java.util.*

@Suppress("DEPRECATION")
class FetchAddressIntentService : IntentService {

    lateinit var resultReceiver : ResultReceiver

    constructor() : super("FetchAddressIntentService")

    override fun onHandleIntent(p0: Intent?) {
        if (p0 != null){
            var errorMessage : String = ""
            resultReceiver = p0.getParcelableExtra<ResultReceiver>(Constants.RECEIVER)!!
            var location = p0.getParcelableExtra<Location>(Constants.LOCATION_DATA_EXTRA)
            if (location == null){
                return
            }

            var geocoder = Geocoder(this, Locale.getDefault())
            var addresses : List<Address>? = null

            try {
                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            } catch (exception : Exception){
                errorMessage = exception.message!!
            }

            if (addresses == null || addresses.isEmpty()){
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage)
            } else {
                var address = addresses[0]
                var addressFragments = arrayListOf<String>()

                for (i in 0..address.maxAddressLineIndex) {
                    addressFragments.add(address.getAddressLine(i))
                }

                deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(Objects
                    .requireNonNull(System.getProperty("line.separator")), addressFragments))

            }
        }
    }

    fun deliverResultToReceiver(resultCode : Int, addressMessage : String){
        var bundle = Bundle()
        bundle.putString(Constants.RESULT_DATA_KEY, addressMessage)
        resultReceiver.send(resultCode, bundle)
    }
}