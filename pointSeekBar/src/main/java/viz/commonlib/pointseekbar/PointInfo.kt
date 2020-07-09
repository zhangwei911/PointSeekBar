package viz.commonlib.pointseekbar

import android.graphics.Color
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PointInfo(
    var progress: Int = 0,
    var color: Int = Color.GRAY,
    var desc: String = ""
) : Parcelable
