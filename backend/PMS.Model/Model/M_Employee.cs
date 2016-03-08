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
    public class M_Employee:IModel
    {
        public M_Employee()
        {
        }


        public static PageData GetEmployeeByDeptid(string deptid)
        {
            return ws.data.jsonDal.JSONDAL.Query("sys_employee", "[{'col':'dept','logic':'=','val':'" + deptid + "','andor':''}]", 1, 9999, "[{'col':'iid','sort':'asc'}]");
        }
    }
}
