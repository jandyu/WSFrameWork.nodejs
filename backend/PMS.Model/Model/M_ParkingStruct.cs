using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ws.data.jsonDal;
namespace PMS.Model
{
    /// <summary>
    /// 建筑物结构
    /// </summary>
    public class M_ParkingStruct:IModel
    {
        public M_ParkingStruct()
        {
        }

        public static PageData GetParkingTree()
        {
            return ws.data.jsonDal.JSONDAL.Query("v_park_info", "[{'col':'iid','logic':'not like','val':'%单元%','andor':''}]", 1, 9999, "[{'col':'title','sort':'asc'}]");
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="parkid"></param>
        /// <returns></returns>
        public static PageData GetParkingInfo(int parkid)
        {
            return ws.data.jsonDal.JSONDAL.Query("v_parking_info", "[{'col':'park','logic':'=','val':'" + parkid.ToString() + "','andor':''}]", 1, 9999, "[{'col':'parking','sort':'asc'}]");
        }
    }
}
