package sylu.com.baidumap.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Mapinfo implements Parcelable {
    public String geoAddr;
    public  double[] geoCoordinates;
    public transient String pwd;//不会被序列化
    //    double latitude10;
//    double longitude;
    protected Mapinfo(Parcel in) {
        geoAddr=in.readString();
        in.readDoubleArray(geoCoordinates);
    }

    public static final Creator<Mapinfo> CREATOR = new Creator<Mapinfo>() {
        @Override
        public Mapinfo createFromParcel(Parcel in) {
            return new Mapinfo(in);
        }

        @Override
        public Mapinfo[] newArray(int size) {
            return new Mapinfo[size];
        }
    };

    public Mapinfo(String geoAddr, double[] geoCoordinates) {
        this.geoAddr = geoAddr;
        this.geoCoordinates = geoCoordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(geoAddr);
        dest.writeDoubleArray(geoCoordinates);
    }
}
