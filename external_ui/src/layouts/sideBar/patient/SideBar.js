
import {Link, useNavigate} from 'react-router-dom';
import RoutesConstant from "../../../RoutesConstant";
import {SendApiService} from "../../../service/SendApiService";
import {AUTHENTICATION} from "../../../ApiConstant";

function SideBar({patient}){
    const currentPath = window.location.pathname
    const navigate = useNavigate()

    const onClickLogout = () => {
        const logoutRequest = {
            accessToken: localStorage.getItem('access-token'),
            refreshToken: localStorage.getItem('refresh-token')
        }

        SendApiService.postRequest(AUTHENTICATION.logout(), logoutRequest, {}, response => {
            localStorage.removeItem('access-token')
            localStorage.removeItem('refresh-token')
            localStorage.removeItem('patient')
            navigate(RoutesConstant.PATIENT.DASHBOARD)
            window.location.reload();
        }, error => {

        })
    }

    return (
        <ul className="space-y-2 font-medium">
            {patient && <>
                <li>
                    <Link
                        to={RoutesConstant.PATIENT.DIAGNOSTICS}
                        className={currentPath.startsWith(RoutesConstant.PATIENT.DIAGNOSTICS) ? 'flex items-center p-2 text-white rounded-lg dark:text-white bg-green-700 group'
                            : 'flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group'}>
                        <svg
                            className="w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                            aria-hidden="true"
                            xmlns="http://www.w3.org/2000/svg"
                            fill="currentColor"
                            viewBox="0 0 22 21">
                            <path
                                d="M16.975 11H10V4.025a1 1 0 0 0-1.066-.998 8.5 8.5 0 1 0 9.039 9.039.999.999 0 0 0-1-1.066h.002Z"/>
                            <path
                                d="M12.5 0c-.157 0-.311.01-.565.027A1 1 0 0 0 11 1.02V10h8.975a1 1 0 0 0 1-.935c.013-.188.028-.374.028-.565A8.51 8.51 0 0 0 12.5 0Z"/>
                        </svg>
                        <span className="ms-3">Chuẩn đoán bệnh</span>
                    </Link>
                </li>
                <li>
                    <Link
                        to={RoutesConstant.PATIENT.APPOINTMENTS_MANAGEMENT}
                        className={currentPath.startsWith(RoutesConstant.PATIENT.APPOINTMENTS_MANAGEMENT) ? 'flex items-center p-2 text-white rounded-lg dark:text-white bg-green-700 group'
                            : 'flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group'}>
                        <svg
                            className="w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                            aria-hidden="true"
                            xmlns="http://www.w3.org/2000/svg"
                            fill="currentColor"
                            viewBox="0 0 22 21">
                            <path
                                d="M16.975 11H10V4.025a1 1 0 0 0-1.066-.998 8.5 8.5 0 1 0 9.039 9.039.999.999 0 0 0-1-1.066h.002Z"/>
                            <path
                                d="M12.5 0c-.157 0-.311.01-.565.027A1 1 0 0 0 11 1.02V10h8.975a1 1 0 0 0 1-.935c.013-.188.028-.374.028-.565A8.51 8.51 0 0 0 12.5 0Z"/>
                        </svg>
                        <span className="ms-3">Quản lý lịch hẹn khám bệnh</span>
                    </Link>
                </li>
                <li>
                    <Link
                        to={RoutesConstant.PATIENT.EXAMINATION_HISTORIES}
                        className={currentPath.startsWith(RoutesConstant.PATIENT.EXAMINATION_HISTORIES) ? 'flex items-center p-2 text-white rounded-lg dark:text-white bg-green-700 group'
                            : 'flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group'}>
                        <svg
                            className="w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                            aria-hidden="true"
                            xmlns="http://www.w3.org/2000/svg"
                            fill="currentColor"
                            viewBox="0 0 22 21">
                            <path
                                d="M16.975 11H10V4.025a1 1 0 0 0-1.066-.998 8.5 8.5 0 1 0 9.039 9.039.999.999 0 0 0-1-1.066h.002Z"/>
                            <path
                                d="M12.5 0c-.157 0-.311.01-.565.027A1 1 0 0 0 11 1.02V10h8.975a1 1 0 0 0 1-.935c.013-.188.028-.374.028-.565A8.51 8.51 0 0 0 12.5 0Z"/>
                        </svg>
                        <span className="ms-3">Lịch sử khám bệnh</span>
                    </Link>
                </li>
                <li>
                    <Link
                        to={RoutesConstant.PATIENT.CHAT}
                        className={currentPath.startsWith(RoutesConstant.PATIENT.CHAT) ? 'flex items-center p-2 text-white rounded-lg dark:text-white bg-green-700 group'
                            : 'flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group'}>
                        <svg
                            className="w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                            aria-hidden="true"
                            xmlns="http://www.w3.org/2000/svg"
                            fill="currentColor"
                            viewBox="0 0 22 21">
                            <path
                                d="M16.975 11H10V4.025a1 1 0 0 0-1.066-.998 8.5 8.5 0 1 0 9.039 9.039.999.999 0 0 0-1-1.066h.002Z"/>
                            <path
                                d="M12.5 0c-.157 0-.311.01-.565.027A1 1 0 0 0 11 1.02V10h8.975a1 1 0 0 0 1-.935c.013-.188.028-.374.028-.565A8.51 8.51 0 0 0 12.5 0Z"/>
                        </svg>
                        <span className="ms-3">Tin nhắn</span>
                    </Link>
                </li>
                <li>
                    <button
                        onClick={() => onClickLogout()}
                        className="w-full flex items-center p-2 text-red-900 bg-red-50 rounded-lg hover:bg-red-100 group">
                        <svg
                            className="w-5 h-5 text-red-600 group-hover:text-red-800"
                            xmlns="http://www.w3.org/2000/svg"
                            fill="none"
                            viewBox="0 0 24 24"
                            stroke="currentColor"
                            strokeWidth="2">
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                d="M15 12H3m0 0l4-4m-4 4l4 4m13-4h-6"
                            />
                        </svg>
                        <span className="ms-3">Đăng xuất</span>
                    </button>
                </li>
            </>}
        </ul>
    )
}

export default SideBar