import styles from '../SideBar.module.css'
import {Link} from 'react-router-dom';
import RoutesConstant from "../../../RoutesConstant";
import {SendApiService} from "../../../service/SendApiService";
import {AUTHENTICATION} from "../../../ApiConstant";

function SideBar({receipt}){
    const onClickLogout = () => {
        const logoutRequest = {
            accessToken: localStorage.getItem('access-token'),
            refreshToken: localStorage.getItem('refresh-token')
        }

        SendApiService.postRequest(AUTHENTICATION.logout(), logoutRequest, {}, response => {
            localStorage.removeItem('access-token')
            localStorage.removeItem('refresh-token')
            localStorage.removeItem('receipt')
            window.location.reload()
        }, error => {

        })
    }

    return (
        <ul className="space-y-2 font-medium">
            {receipt && <>
                <li>
                    <Link
                        to={RoutesConstant.RECEIPT.RECEIPT_PATIENT}
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
                        <span className="ms-3">Tiếp đón bệnh nhân</span>
                    </Link>
                </li>
                <li>
                    <Link
                        to={RoutesConstant.RECEIPT.PAYMENT}
                        className="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
                        <svg
                            className="w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                            aria-hidden="true"
                            xmlns="http://www.w3.org/2000/svg"
                            fill="currentColor"
                            viewBox="0 0 24 24">
                            <path d="M4 6h16v12H4z" opacity="0.4"/>
                            <path d="M22 4H2v16h20V4zM20 16H4v-8h16v8z"/>
                        </svg>
                        <span className="ms-3">Thanh toán</span>
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
                        <span className="ml-3">Đăng xuất</span>
                    </button>
                </li>
            </>}
        </ul>

    )
}

export default SideBar