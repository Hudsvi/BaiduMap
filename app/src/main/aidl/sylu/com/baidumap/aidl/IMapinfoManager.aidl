// IMapinfoManager.aidl
package sylu.com.baidumap.aidl;
// Declare any non-default types here with import statements
import sylu.com.baidumap.aidl.Mapinfo;
interface IMapinfoManager {
  List<Mapinfo> getMapin();
  void addMapinfo(in Mapinfo info);
}
