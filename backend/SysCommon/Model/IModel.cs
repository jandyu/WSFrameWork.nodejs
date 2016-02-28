using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
namespace ws.data.jsonDal//ws.data.jsonDal
{
    [Serializable]
    public class GridCol : ICloneable
    {
        private string name = "";

        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        private string dataPropertyName = "";

        public string DataPropertyName
        {
            get { return dataPropertyName; }
            set { dataPropertyName = value; }
        }


        private string headerText = "";

        public string HeaderText
        {
            get { return headerText; }
            set { headerText = value; }
        }


        private int width = 100;

        public int Width
        {
            get { return width; }
            set { width = value; }
        }

        /// <summary>
        /// 自动充满DataGridView空
        /// </summary>
        private bool autoFill = false;

        public bool AutoFill
        {
            get { return autoFill; }
            set { autoFill = value; }
        }



        private string colType = "TextBox";

        public string ColType
        {
            get { return colType; }
            set { colType = value; }
        }


        private bool visible = true;

        public bool Visible
        {
            get { return visible; }
            set { visible = value; }
        }
        /// <summary>
        /// 下拉字典分类
        /// 如果Edit_Type是dropdownlist,设置
        /// </summary>
        private string dropDownList_Cate = "";

        public string DropDownList_Cate
        {
            get { return dropDownList_Cate; }
            set { dropDownList_Cate = value; }
        }

        /// <summary>
        /// 下拉字典Parent_id
        /// 如果Edit_Type是dropdownlist,设置
        /// </summary>
        private string dropDownList_PID = "";

        public string DropDownList_PID
        {
            get { return dropDownList_PID; }
            set { dropDownList_PID = value; }
        }
        /// <summary>
        /// 下拉字典显示字段
        /// </summary>
        private string dropDownList_DisplayMember = "txt";

        public string DropDownList_DisplayMember
        {
            get { return dropDownList_DisplayMember; }
            set { dropDownList_DisplayMember = value; }
        }
        /// <summary>
        /// 下拉字典值字段
        /// </summary>
        private string dropDownList_ValueMember = "id";

        public string DropDownList_ValueMember
        {
            get { return dropDownList_ValueMember; }
            set { dropDownList_ValueMember = value; }
        }

        /// <summary>
        /// 显示格式，比如日期
        /// 如果是日期，则DataType=datetime,CellFormat=yyyy-MM-dd
        /// 如果是databutton,则CellFormat=0:挂牌;1:撤销
        /// </summary>
        private string cellFormat = "";

        /// <summary>
        /// 显示格式，比如日期
        /// 如果是日期，则DataType=datetime,CellFormat=yyyy-MM-dd
        /// 如果是databutton,则CellFormat=0:挂牌;1:撤销
        /// </summary>
        public string CellFormat
        {
            get { return cellFormat; }
            set { cellFormat = value; }
        }

        /// <summary>
        /// 数据类型
        /// </summary>
        private string dataType = "string";
        /// <summary>
        /// 数据类型
        /// </summary>
        public string DataType
        {
            get { return dataType; }
            set { dataType = value; }
        }

        /// <summary>
        /// 对齐方式
        /// </summary>
        private string align = "center";

        public string Align
        {
            get { return align; }
            set { align = value; }
        }




        public object Clone()
        {
            BinaryFormatter bf = new BinaryFormatter();

            MemoryStream ms = new MemoryStream();

            bf.Serialize(ms, this);

            ms.Seek(0, SeekOrigin.Begin);

            return bf.Deserialize(ms);
        }
    }

    [Serializable]
    public class EditCol : ICloneable
    {
        private string name = "";

        public string Name
        {
            get { return name; }
            set { name = value; }
        }
        private string headerText = "";

        public string HeaderText
        {
            get { return headerText; }
            set { headerText = value; }
        }

        /// <summary>
        /// 宽度
        /// </summary>
        private float headerText_W = 59f;

        public float HeaderText_W
        {
            get { return headerText_W; }
            set { headerText_W = value; }
        }


        private float edit_X = 0;

        public float Edit_X
        {
            get { return edit_X; }
            set { edit_X = value; }
        }
        private float edit_Y = 0;

        public float Edit_Y
        {
            get { return edit_Y; }
            set { edit_Y = value; }
        }
        private float edit_W = 1;

        public float Edit_W
        {
            get { return edit_W; }
            set { edit_W = value; }
        }
        private float edit_H = 1;

        public float Edit_H
        {
            get { return edit_H; }
            set { edit_H = value; }
        }


        /// <summary>
        /// 是否为表中的字段
        /// </summary>
        private bool edit_IsOrNoTableCol = true;

        public bool Edit_IsOrNoTableCol
        {
            get { return edit_IsOrNoTableCol; }
            set { edit_IsOrNoTableCol = value; }
        }
        /// <summary>
        /// 编辑的窗口中是否显示
        /// </summary>
        private bool edit_Visible = true;

        public bool Edit_Visible
        {
            get { return edit_Visible; }
            set { edit_Visible = value; }
        }


        /// <summary>
        /// 编辑窗口中控件是否可用
        /// </summary>
        private bool edit_Enabled = true;

        public bool Edit_Enabled
        {
            get { return edit_Enabled; }
            set { edit_Enabled = value; }
        }

        /// <summary>
        /// 编辑值
        /// </summary>
        private string edit_Val = "";

        public string Edit_Val
        {
            get { return edit_Val; }
            set { edit_Val = value; }
        }

        /// <summary>
        /// 编辑框类型默认为textbox
        /// 类型:textbox,dropdownlist
        /// </summary>
        private string edit_Type = "textbox";

        public string Edit_Type
        {
            get { return edit_Type; }
            set { edit_Type = value; }
        }

        /// <summary>
        /// 输入框单行还是多行
        /// </summary>
        private bool edit_Multiline = false;

        public bool Edit_Multiline
        {
            get { return edit_Multiline; }
            set { edit_Multiline = value; }
        }

        /// <summary>
        /// 下拉字典分类
        /// 如果Edit_Type是dropdownlist,设置
        /// </summary>
        private string edit_DropDownList_Cate = "";

        public string Edit_DropDownList_Cate
        {
            get { return edit_DropDownList_Cate; }
            set { edit_DropDownList_Cate = value; }
        }

        /// <summary>
        /// 下拉类型：DropDown和DropDownList
        /// </summary>
        private string edit_DropDownList_Style = "dropdownlist";

        public string Edit_DropDownList_Style
        {
            get { return edit_DropDownList_Style; }
            set { edit_DropDownList_Style = value; }
        }

        /// <summary>
        /// 下拉字典Parent_id
        /// 如果Edit_Type是dropdownlist,设置
        /// </summary>
        private string edit_DropDownList_PID = "";

        public string Edit_DropDownList_PID
        {
            get { return edit_DropDownList_PID; }
            set { edit_DropDownList_PID = value; }
        }


        /// <summary>
        /// 下拉字典、下拉树显示字段
        /// </summary>
        private string edit_DropDownListTree_DisplayMember = "txt";

        public string Edit_DropDownListTree_DisplayMember
        {
            get { return edit_DropDownListTree_DisplayMember; }
            set { edit_DropDownListTree_DisplayMember = value; }
        }
        /// <summary>
        /// 下拉字典、下拉树值字段
        /// </summary>
        private string edit_DropDownListTree_ValueMember = "id";

        public string Edit_DropDownListTree_ValueMember
        {
            get { return edit_DropDownListTree_ValueMember; }
            set { edit_DropDownListTree_ValueMember = value; }
        }


        /// <summary>
        /// 如果下拉树点击后，textbox显示的是另外一个字段则设置此属性,例如order_id
        /// </summary>
        private string edit_DropDownTreeShowTxtField = "txt";

        public string Edit_DropDownTreeShowTxtField
        {
            get { return edit_DropDownTreeShowTxtField; }
            set { edit_DropDownTreeShowTxtField = value; }
        }

        /// <summary>
        /// 树控件的默认提示信息
        /// </summary>
        private string edit_DropDownTreeTipText = "请选择";

        public string Edit_DropDownTreeTipText
        {
            get { return edit_DropDownTreeTipText; }
            set { edit_DropDownTreeTipText = value; }
        }

        /// <summary>
        /// 必须双击叶子节点才有效
        /// </summary>
        private bool edit_DropDownTreeMustClickLeafNode = true;

        public bool Edit_DropDownTreeMustClickLeafNode
        {
            get { return edit_DropDownTreeMustClickLeafNode; }
            set { edit_DropDownTreeMustClickLeafNode = value; }
        }





        /// <summary>
        /// 默认值--新增时的默认值
        /// </summary>
        private string edit_DefaultValue = "";

        public string Edit_DefaultValue
        {
            get { return edit_DefaultValue; }
            set { edit_DefaultValue = value; }
        }

        /// <summary>
        /// 是否为保存字段--默认时保存时要修改的字段
        /// </summary>
        private bool edit_SaveCol = true;

        public bool Edit_SaveCol
        {
            get { return edit_SaveCol; }
            set { edit_SaveCol = value; }
        }

        /// <summary>
        /// 校验正在表达式
        /// </summary>
        private string edit_Regular = "";

        public string Edit_Regular
        {
            get { return edit_Regular; }
            set { edit_Regular = value; }
        }

        /// <summary>
        /// 正则表达式校验未通过时的提醒信息
        /// </summary>
        private string edit_Regulay_Msg = "";

        public string Edit_Regulay_Msg
        {
            get { return edit_Regulay_Msg; }
            set { edit_Regulay_Msg = value; }
        }

        /// <summary>
        /// 显示格式:日期
        /// </summary>
        private string edit_Format = "";

        public string Edit_Format
        {
            get { return edit_Format; }
            set { edit_Format = value; }
        }
        /// <summary>
        /// 所属容器
        /// </summary>
        private string edit_Parent_Container = "";

        public string Edit_Parent_Container
        {
            get { return edit_Parent_Container; }
            set { edit_Parent_Container = value; }
        }

        /// <summary>
        /// 图片列表的类型
        /// </summary>
        private string edit_ImageListCate = "";

        public string Edit_ImageListCate
        {
            get { return edit_ImageListCate; }
            set { edit_ImageListCate = value; }
        }
        /// <summary>
        /// 图片列表是否显示每一个图片的标题
        /// </summary>
        private bool edit_ImageListShowTitle = false;

        public bool Edit_ImageListShowTitle
        {
            get { return edit_ImageListShowTitle; }
            set { edit_ImageListShowTitle = value; }
        }

        /// <summary>
        /// 是否允许上传图片
        /// </summary>
        private bool edit_ImageListAllowUpload = false;

        public bool Edit_ImageListAllowUpload
        {
            get { return edit_ImageListAllowUpload; }
            set { edit_ImageListAllowUpload = value; }
        }

        /// <summary>
        /// 是否允许删除图片
        /// </summary>
        private bool edit_ImageListAllowDel = false;

        public bool Edit_ImageListAllowDel
        {
            get { return edit_ImageListAllowDel; }
            set { edit_ImageListAllowDel = value; }
        }

        /// <summary>
        /// 是否允许修改图片
        /// </summary>
        private bool edit_ImageListAllowRep = false;

        public bool Edit_ImageListAllowRep
        {
            get { return edit_ImageListAllowRep; }
            set { edit_ImageListAllowRep = value; }
        }


        /// <summary>
        /// Grid配置文件名称，可以带路径用@分割
        /// </summary>
        private string edit_Grid_ConfigName;

        public string Edit_Grid_ConfigName
        {
            get { return edit_Grid_ConfigName; }
            set { edit_Grid_ConfigName = value; }
        }


        /// <summary>
        /// 查询模式，默认是table,另外一种模式是defid,当模式是defid时需要定义defid和frmid
        /// </summary>
        private string edit_Grid_Mode = "table";

        public string Edit_Grid_Mode
        {
            get { return edit_Grid_Mode; }
            set { edit_Grid_Mode = value; }
        }
        /// <summary>
        /// 表名
        /// </summary>
        private string edit_Grid_TableName;

        public string Edit_Grid_TableName
        {
            get { return edit_Grid_TableName; }
            set { edit_Grid_TableName = value; }
        }
        /// <summary>
        /// defid
        /// </summary>
        private string eidt_Grid_defid;

        public string Eidt_Grid_defid
        {
            get { return eidt_Grid_defid; }
            set { eidt_Grid_defid = value; }
        }
        /// <summary>
        /// frmid
        /// </summary>
        private string eidt_Grid_frmid = "json";

        public string Eidt_Grid_frmid
        {
            get { return eidt_Grid_frmid; }
            set { eidt_Grid_frmid = value; }
        }
        /// <summary>
        /// 排序信息
        /// {'col':'iid','sort':'asc'}或者[{'col':'iid','sort':'asc'},{'col':'name','sort':'asc'}]
        /// </summary>
        private string eidt_Grid_Sort = "{'col':'iid','sort':'asc'}";

        public string Eidt_Grid_Sort
        {
            get { return eidt_Grid_Sort; }
            set { eidt_Grid_Sort = value; }
        }

        /// <summary>
        /// 每页显示的行数
        /// </summary>
        private int eidt_Grid_PageSize = 50;

        public int Eidt_Grid_PageSize
        {
            get { return eidt_Grid_PageSize; }
            set { eidt_Grid_PageSize = value; }
        }

        /// <summary>
        /// 是否显示分页信息
        /// </summary>
        private bool eidt_Grid_ShowPager = true;

        public bool Eidt_Grid_ShowPager
        {
            get { return eidt_Grid_ShowPager; }
            set { eidt_Grid_ShowPager = value; }
        }


        public object Clone()
        {
            BinaryFormatter bf = new BinaryFormatter();

            MemoryStream ms = new MemoryStream();

            bf.Serialize(ms, this);

            ms.Seek(0, SeekOrigin.Begin);

            return bf.Deserialize(ms);
        }
    }    

    public class IModel
    {
        /// <summary>
        /// 添加
        /// </summary>
        /// <param name="tablename"></param>
        /// <param name="Dic_Cols"></param>
        /// <param name="msg"></param>
        public string addModel(string tablename, Dictionary<string, EditCol> Dic_Cols,ref string msg)
        {
            try
            {
                StringBuilder sb = new StringBuilder();
                sb.Append("{");
                string edit_type="";
                foreach (var v in Dic_Cols.Values)
                {
                    edit_type=v.Edit_Type.ToLower();
                    if (v.Edit_SaveCol && edit_type != "button" && edit_type != "datagridview" )
                    {
                        if (v.Edit_Type == "datetime" && v.Edit_Val=="")
                        {
                            continue;
                        }
                        sb.AppendFormat("'{0}':'{1}',", v.Name.ToLower(), v.Edit_Val);
                    }
                }
                sb.Remove(sb.Length - 1, 1);//移除最后一个逗号
                sb.Append("}");
                return ws.data.jsonDal.JSONDAL.Insert(tablename,sb.ToString(), ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
                return "";
            }
        }

        /// <summary>
        /// 修改
        /// </summary>
        /// <param name="tablename"></param>
        /// <param name="Dic_Cols"></param>
        /// <param name="msg"></param>
        public void updModel(string tablename, Dictionary<string, EditCol> Dic_Cols, ref string msg)
        {
            try
            {
                StringBuilder sb = new StringBuilder();
                sb.Append("{");
                string edit_type = "";
                foreach (var v in Dic_Cols.Values)
                {
                    edit_type = v.Edit_Type.ToLower();
                    if (edit_type == "button" || edit_type == "datagridview")
                    {
                        continue;
                    }
                    
                    if (v.Edit_SaveCol ||  v.Name.ToLower()=="iid")
                    {
                        if (v.Edit_Type == "datetime" && v.Edit_Val == "")
                        {
                            continue;
                        }
                        sb.AppendFormat("'{0}':'{1}',", v.Name.ToLower() == "iid" ? "key_iid" : v.Name.ToLower(), v.Edit_Val);
                    }
                }
                sb.Remove(sb.Length - 1, 1);//移除最后一个逗号
                sb.Append("}");
                ws.data.jsonDal.JSONDAL.Update(tablename,sb.ToString(), ref msg);
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
        }

        /// <summary>
        /// 删除
        /// </summary>
        /// <param name="tablename"></param>
        /// <param name="iids"></param>
        /// <param name="msg"></param>
        public void delModel(string tablename,string iids , ref string msg)
        {
            ws.data.jsonDal.JSONDAL.Delete(tablename, iids, ref msg);
        }
    }
}
