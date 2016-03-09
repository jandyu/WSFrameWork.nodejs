
#region
namespace MSGSrv.Models
{
    using System;
    using SN.Data;
    using System.ComponentModel.DataAnnotations;

    [SerializableAttribute()]
    public partial class app_resource_message : Entity
    {
        /// <summary>
        /// 
        /// </summary>
        protected Int64 _iid;
        /// <summary>
        /// 
        /// </summary>
        protected Int32? _target_type;
        /// <summary>
        /// 
        /// </summary>
        protected String _target;
        /// <summary>
        /// 
        /// </summary>
        protected String _title;
        /// <summary>
        /// 
        /// </summary>
        protected String _info;
        /// <summary>
        /// 
        /// </summary>
        protected DateTime? _send_time;
        /// <summary>
        /// 
        /// </summary>
        protected String _status;
        /// <summary>
        /// 
        /// </summary>
        protected Int32? _try_times;
        /// <summary>
        /// 
        /// </summary>
        protected String _status_message;
        /// <summary>
        /// 
        /// </summary>
        protected String _app_id;

        /// <summary>
        /// 
        /// </summary>
        public Int64 Iid
        {
            get { return this._iid; }
            set
            {
                this.OnPropertyValueChange(_.iid, _iid, value);
                this._iid = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public Int32? Target_type
        {
            get { return this._target_type; }
            set
            {
                this.OnPropertyValueChange(_.target_type, _target_type, value);
                this._target_type = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public String Target
        {
            get { return this._target; }
            set
            {
                this.OnPropertyValueChange(_.target, _target, value);
                this._target = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public String Title
        {
            get { return this._title; }
            set
            {
                this.OnPropertyValueChange(_.title, _title, value);
                this._title = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public String Info
        {
            get { return this._info; }
            set
            {
                this.OnPropertyValueChange(_.info, _info, value);
                this._info = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public DateTime? Send_time
        {
            get { return this._send_time; }
            set
            {
                this.OnPropertyValueChange(_.send_time, _send_time, value);
                this._send_time = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public String Status
        {
            get { return this._status; }
            set
            {
                this.OnPropertyValueChange(_.status, _status, value);
                this._status = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public Int32? Try_times
        {
            get { return this._try_times; }
            set
            {
                this.OnPropertyValueChange(_.try_times, _try_times, value);
                this._try_times = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public String Status_message
        {
            get { return this._status_message; }
            set
            {
                this.OnPropertyValueChange(_.status_message, _status_message, value);
                this._status_message = value;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public String App_id
        {
            get { return this._app_id; }
            set
            {
                this.OnPropertyValueChange(_.app_id, _app_id, value);
                this._app_id = value;
            }
        }


        /// <summary>
        /// 获取实体对应的表名
        /// </summary>
        protected override Table GetTable()
        {
            return new Table<app_resource_message>("app_resource_message");
        }


        /// <summary>
        /// 获取实体中的标识列
        /// </summary>
        protected override Field GetIdentityField()
        {
            return _.iid;
        }

        /// <summary>
        /// 获取实体中的主键列
        /// </summary>
        protected override Field[] GetPrimaryKeyFields()
        {
            return new Field[] {
                        _.iid
                    };
        }

        /// <summary>
        /// 获取列信息
        /// </summary>
        protected override Field[] GetFields()
        {
            return new Field[] {
                    _.iid,
                    _.target_type,
                    _.target,
                    _.title,
                    _.info,
                    _.send_time,
                    _.status,
                    _.try_times,
                    _.status_message,
                    _.app_id};
        }

        /// <summary>
        /// 获取列数据
        /// </summary>
        protected override object[] GetValues()
        {
            return new object[] {
                        this._iid,
                        this._target_type,
                        this._target,
                        this._title,
                        this._info,
                        this._send_time,
                        this._status,
                        this._try_times,
                        this._status_message,
                        this._app_id};
        }

        /// <summary>
        /// 给当前实体赋值
        /// </summary>
        protected override void SetValues(IRowReader reader)
        {
            if ((false == reader.IsDBNull(_.iid)))
            {
                this._iid = reader.GetInt64(_.iid);
            }
            if ((false == reader.IsDBNull(_.target_type)))
            {
                this._target_type = reader.GetInt32(_.target_type);
            }
            if ((false == reader.IsDBNull(_.target)))
            {
                this._target = reader.GetString(_.target);
            }
            if ((false == reader.IsDBNull(_.title)))
            {
                this._title = reader.GetString(_.title);
            }
            if ((false == reader.IsDBNull(_.info)))
            {
                this._info = reader.GetString(_.info);
            }
            if ((false == reader.IsDBNull(_.send_time)))
            {
                this._send_time = reader.GetDateTime(_.send_time);
            }
            if ((false == reader.IsDBNull(_.status)))
            {
                this._status = reader.GetString(_.status);
            }
            if ((false == reader.IsDBNull(_.try_times)))
            {
                this._try_times = reader.GetInt32(_.try_times);
            }
            if ((false == reader.IsDBNull(_.status_message)))
            {
                this._status_message = reader.GetString(_.status_message);
            }
            if ((false == reader.IsDBNull(_.app_id)))
            {
                this._app_id = reader.GetString(_.app_id);
            }
        }

        public override int GetHashCode()
        {
            return base.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            if ((obj == null))
            {
                return false;
            }
            if ((false == typeof(app_resource_message).IsAssignableFrom(obj.GetType())))
            {
                return false;
            }
            if ((((object)(this)) == ((object)(obj))))
            {
                return true;
            }
            return false;
        }

        public class _
        {
            /// <summary>
            /// 表示选择所有列，与*等同
            /// </summary>
            public static AllField All = new AllField<app_resource_message>();

            /// <summary>
            ///  - 字段名：iid -  数据类型：Int64
            /// </summary>
            public static Field iid = new Field<app_resource_message>("iid");

            /// <summary>
            ///  - 字段名：target_type -  数据类型：Int32?
            /// </summary>
            public static Field target_type = new Field<app_resource_message>("target_type");

            /// <summary>
            ///  - 字段名：target -  数据类型：String
            /// </summary>
            public static Field target = new Field<app_resource_message>("target");

            /// <summary>
            ///  - 字段名：title -  数据类型：String
            /// </summary>
            public static Field title = new Field<app_resource_message>("title");

            /// <summary>
            ///  - 字段名：info -  数据类型：String
            /// </summary>
            public static Field info = new Field<app_resource_message>("info");

            /// <summary>
            ///  - 字段名：send_time -  数据类型：DateTime?
            /// </summary>
            public static Field send_time = new Field<app_resource_message>("send_time");

            /// <summary>
            ///  - 字段名：status -  数据类型：String
            /// </summary>
            public static Field status = new Field<app_resource_message>("status");

            /// <summary>
            ///  - 字段名：try_times -  数据类型：Int32?
            /// </summary>
            public static Field try_times = new Field<app_resource_message>("try_times");

            /// <summary>
            ///  - 字段名：status_message -  数据类型：String
            /// </summary>
            public static Field status_message = new Field<app_resource_message>("status_message");

            /// <summary>
            ///  - 字段名：app_id -  数据类型：String
            /// </summary>
            public static Field app_id = new Field<app_resource_message>("app_id");

        }
    }  //class end
} //namespace end
#endregion

