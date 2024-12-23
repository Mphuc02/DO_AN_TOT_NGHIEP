import styles from '../SideBar.module.css'
import {Link} from 'react-router-dom';
import {SendApiService} from "../../../service/SendApiService";
import {AUTHENTICATION} from "../../../ApiConstant";

function SideBar(){
    const currentPath = window.location.pathname

    const onClickLogout = () => {
        const logoutRequest = {
            accessToken: localStorage.getItem('access-token'),
            refreshToken: localStorage.getItem('refresh-token')
        }

        SendApiService.postRequest(AUTHENTICATION.logout(), logoutRequest, {}, response => {
            localStorage.removeItem('access-token')
            localStorage.removeItem('refresh-token')
            localStorage.removeItem('admin')
            window.location.reload()
        }, error => {

        })
    }

    return(
        <ul className={styles.SideBar}>
            <li><Link to={"/admin/employee-management"}
                      className="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
                <svg
                    className="w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                    aria-hidden="true"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="currentColor"
                    viewBox="0 0 24 24">
                    <path
                        d="M12 2a6 6 0 100 12 6 6 0 000-12zm0 10a4 4 0 110-8 4 4 0 010 8zM2 20a10 10 0 0120 0H2z"/>
                </svg>
                <span className="ms-3">Quản lý thông tin nhân viên</span></Link></li>
            <li>
                <Link to={"/admin/medicine-management"}>Quản lý thông tin thuốc</Link>

                {currentPath.startsWith('/admin/medicine-management') &&
                    <ul>
                        <li><Link to={'/admin/medicine-management/origin'}>Quản lý nguồn gôc thuốc</Link></li>
                    </ul>}
            </li>
            <li><Link to={'/admin/examination-room-management'}>Quản lý thông tin phòng khám</Link></li>
            <li><a href="/admin/">Profile</a></li>
        </ul>
    )
}

export default SideBar