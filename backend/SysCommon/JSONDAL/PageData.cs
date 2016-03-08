using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using System.IO;
using System.Data;
using Newtonsoft.Json.Linq;
namespace ws.data.jsonDal
{

    /// <summary>
    /// 查询结果（分页和datatable）
    /// </summary>
    public class PageData
    {
        #region

        /// <summary>
        /// 每页行数
        /// </summary>
        public int i_pagesize = 1000;
        /// <summary>
        /// 总行数
        /// </summary>
        public int i_totalrows = 0;
        /// <summary>
        /// 当前页
        /// </summary>
        public int i_currpage = 0;
        /// <summary>
        /// 总页数
        /// </summary>
        public int i_totalpages = 0;
        /// <summary>
        /// 查询出来的数据表
        /// </summary>
        public DataTable dt_data = null;

        /// <summary>
        /// 
        /// </summary>
        /// <param name="pagesize"></param>
        /// <param name="totalrows"></param>
        /// <param name="currpage"></param>
        /// <param name="totalpages"></param>
        /// <param name="dt"></param>
        public PageData(int pagesize, int totalrows, int currpage, int totalpages, DataTable dt)
        {
            i_pagesize = pagesize;
            i_totalrows = totalrows;
            i_currpage = currpage;
            i_totalpages = totalpages;
            dt_data = dt;
        }

        public PageData()
        {
        }
        #endregion
    }
}
