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
    public class M_RoomStruct:IModel
    {
        public M_RoomStruct()
        {
        }

        /// <summary>
        /// 房屋树结构
        /// </summary>
        /// <returns></returns>
        public static PageData GetRoomTree()
        {
            //return ws.data.jsonDal.JSONDAL.Query("app_wy_unit_building", "[{'col':'iid','logic':'not like','val':'%单元%','andor':' '}]", 1, 9999, "[{'col':'title','sort':'asc'}]");
            return ws.data.jsonDal.JSONDAL.Query("app_wy_unit_building", "[{'col':'title','logic':'not like','val':'%单元%','andor':' and '},{'col':'iid','logic':'<>','val':'182','andor':' and '},{'col':'iid','logic':'<>','val':'183','andor':' '}]", 1, 9999, "[{'col':'title','sort':'asc'}]");
        }

        /// <summary>
        /// 获取建筑物信息(建筑物包括两种情况(1、有单元；2、无单元))
        /// </summary>
        /// <param name="buildingid"></param>
        /// <returns></returns>
        public static PageData GetBuildInfo(int buildingid)
        {
            return ws.data.jsonDal.JSONDAL.Query("v_build_info", "[{'col':'buildingid','logic':'=','val':'" + buildingid.ToString() + "','andor':''}]", 1, 9999, "[{'col':'unit','sort':'asc'}]");
        }

        /// <summary>
        /// 获取房间信息
        /// </summary>
        /// <param name="buildingid"></param>
        /// <returns></returns>
        public static PageData GetRoomInfoByBuild(int buildingid)
        {
            return ws.data.jsonDal.JSONDAL.Query("v_room_info", "[{'col':'buildingid','logic':'=','val':'" + buildingid.ToString() + "','andor':''}]", 1, 9999, "[{'col':'room','sort':'asc'}]");
        }

        /// <summary>
        /// 获取房间信息
        /// </summary>
        /// <param name="buildingid"></param>
        /// <returns></returns>
        public static PageData GetRoomInfoByUnit(int unitid)
        {
            return ws.data.jsonDal.JSONDAL.Query("v_room_info", "[{'col':'unitid','logic':'=','val':'" + unitid.ToString() + "','andor':''}]", 1, 9999, "[{'col':'room','sort':'asc'}]");
        }

    }
}
