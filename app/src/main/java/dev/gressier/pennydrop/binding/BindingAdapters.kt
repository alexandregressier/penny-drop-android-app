package dev.gressier.pennydrop.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.Converters
import dev.gressier.pennydrop.R

@BindingAdapter("isHidden")
fun bindIsHidden(view: View, isInvisible: Boolean) {
    view.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("playerSummaryAvatarSrc")
fun bindPlayerSummaryAvatarSrc(imageView: ImageView, isHuman: Boolean) {
    imageView.setImageResource(
        if (isHuman) R.drawable.mdi_face_man_black_24dp
        else R.drawable.mdi_robot_black_24dp
    )
}

@BindingAdapter("playerSummaryAvatarTint")
fun bindPlayerSummaryAvatarTint(imageView: ImageView, isHuman: Boolean) {
    imageView.imageTintList = Converters.convertColorToColorStateList(
        imageView.context.getColor(
            if (isHuman) android.R.color.holo_blue_dark
            else android.R.color.holo_green_dark
        )
    )
}

@BindingAdapter("slotLastRolled")
fun bindSlotLastRolled(view: View, lastRolled: Boolean) {
    view.isActivated = lastRolled
}