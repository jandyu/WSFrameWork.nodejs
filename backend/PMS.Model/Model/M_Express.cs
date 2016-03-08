using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ws.data.jsonDal;
namespace PMS.Model
{
    /// <summary>
    /// 员工管理
    /// </summary>
    public class M_Express:IModel
    {
        public M_Express()
        {
        }

        
        /// <summary>
        /// 获取快递统计信息
        /// </summary>
        /// <returns></returns>
        public static PageData getTJXX()
        {
            return ws.data.jsonDal.JSONDAL.Query("v_express_tj", "[]", 1, 9999, "[{'col':'iid','sort':'asc'}]");
        }
    }
}
