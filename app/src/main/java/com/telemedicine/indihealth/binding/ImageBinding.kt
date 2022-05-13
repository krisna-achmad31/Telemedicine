package id.co.pradiptapaa.icare.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.helper.GlideApp
import de.hdodenhof.circleimageview.CircleImageView
import timber.log.Timber


@BindingAdapter("setImageDrawable")
fun setImageDrawable(view: ImageView, int: Int) {
    view.setImageResource(int)
}

@BindingAdapter("setImageUrl")
fun setImageUrl(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        GlideApp
            .with(view)
            .load(url)
            .placeholder(R.drawable.placeholder_square)
            .into(view)
    } else {
        GlideApp
            .with(view)
            .load(R.drawable.placeholder_square)
            .into(view)
    }
}


@BindingAdapter("setImageUrlNews")
fun setImageUrlNews(view: ImageView, url: String?) {
    Timber.d("URL NYA = ${AppVar.BASE_NEWS_URL}$url")
    if (!url.isNullOrEmpty()) {
        Glide
            .with(view)
            .load(AppVar.BASE_NEWS_URL+url)
            //.load("https://idok.telemedical.id/assets/images/news/news_2.jpeg")
            .placeholder(R.drawable.placeholder_square)
            .into(view)
            .apply { RequestOptions.timeoutOf(6000) }
    } else {
        GlideApp
            .with(view)
            .load(R.drawable.placeholder_square)
            .into(view)
    }
}

@BindingAdapter("setCircleImageUrlWithBaseUrl")
fun setCircleImageUrlWithBaseUrl(view: CircleImageView, url: String?) {
    Timber.d("the url is = $url")
    if (!url.isNullOrBlank()) {
        Timber.d("the url is = ${AppVar.BASE_FILE_URL+url}")
        GlideApp
            .with(view)
            .load(AppVar.BASE_FILE_URL+url)
            .override(80, 80)
            .placeholder(R.drawable.placeholder_square)
            .into(view)
    } else {
        GlideApp
            .with(view)
            .load(R.drawable.placeholder_square)
            .into(view)
    }
}

@BindingAdapter("setImageBaseUrl")
fun setImageBaseUrl(view: ImageView, url: String?) {
    if (!url.isNullOrBlank()) {
        GlideApp
            .with(view)
            .load(AppVar.BASE_FILE_URL+url)
            .placeholder(R.drawable.placeholder_square)
            .into(view)
    } else {
        GlideApp
            .with(view)
            .load(R.drawable.placeholder_square)
            .into(view)
    }
}

@BindingAdapter("setImageBaseUrlFiles")
fun setImageBaseUrlFiles(view: ImageView, url: String?) {
    if (!url?.substringAfterLast("/").isNullOrEmpty()) {
        GlideApp
            .with(view)
            .load(AppVar.BASE_FILES_URL+url)
            .placeholder(R.drawable.placeholder_square)
            .into(view)
    } else {
        GlideApp
            .with(view)
            .load(R.drawable.placeholder_square)
            .into(view)
    }
}

@BindingAdapter("setImageUrlWithBaseUrlHistoryConsul")
fun setImageUrlWithBaseUrlHistoryConsul(view: ImageView, url: String?) {
    Timber.d("the url is = $url")
    if (!url.isNullOrBlank()) {
        Timber.d("the url is = ${AppVar.BASE_FILES_HISTORY_CONSULTATION_URL+url}")
        GlideApp
            .with(view)
            .load(AppVar.BASE_FILES_HISTORY_CONSULTATION_URL+url)
            .placeholder(R.drawable.placeholder_square)
            .into(view)
    } else {
        GlideApp
            .with(view)
            .load(R.drawable.placeholder_square)
            .into(view)
    }
}

@BindingAdapter("setImageUrlWithBaseUrlHistoryDrug")
fun setImageUrlWithBaseUrlHistoryDrug(view: ImageView, url: String?) {
    Timber.d("the url is = $url")
    if (!url.isNullOrBlank()) {
        Timber.d("the url is = ${AppVar.BASE_FILES_HISTORY_DRUG_URL+url}")
        GlideApp
            .with(view)
            .load(AppVar.BASE_FILES_HISTORY_DRUG_URL+url)
            .placeholder(R.drawable.placeholder_square)
            .into(view)
    } else {
        GlideApp
            .with(view)
            .load(R.drawable.placeholder_square)
            .into(view)
    }
}

@BindingAdapter("setCircleImageUrlWithBaseUrlHistoryConsul")
fun setCircleImageUrlWithBaseUrlHistoryConsul(view: CircleImageView, url: String?) {
    Timber.d("the url is = $url")
    if (!url.isNullOrBlank()) {
        Timber.d("the url is = ${AppVar.BASE_FILES_HISTORY_CONSULTATION_URL+url}")
        GlideApp
            .with(view)
            .load(AppVar.BASE_FILES_HISTORY_CONSULTATION_URL+url)
            .override(80, 80)
            .placeholder(R.drawable.placeholder_square)
            .into(view)
    } else {
        GlideApp
            .with(view)
            .load(R.drawable.placeholder_square)
            .into(view)
    }
}

@BindingAdapter("setCircleImageUrlWithBaseUrlHistoryDrug")
fun setCircleImageUrlWithBaseUrlHistoryDrug(view: CircleImageView, url: String?) {
    Timber.d("the url is = $url")
    if (!url.isNullOrBlank()) {
        Timber.d("the url is = ${AppVar.BASE_FILES_HISTORY_DRUG_URL+url}")
        GlideApp
            .with(view)
            .load(AppVar.BASE_FILES_HISTORY_DRUG_URL+url)
            .override(80, 80)
            .placeholder(R.drawable.placeholder_square)
            .into(view)
    } else {
        GlideApp
            .with(view)
            .load(R.drawable.placeholder_square)
            .into(view)
    }
}
