import logo from "../../imgs/logo.jpg";
import {useState} from "react";
import ToastPopup from "../common/ToastPopup";
import {Link} from "react-router-dom";
import RoutesConstant from "../../RoutesConstant";

const SignUp = () => {
    const [data, setData] = useState({})
    const [labelError, setLabelError] = useState('')
    const [toast, setToast] = useState(null)
    const hideToast = () => {
        setToast(null);
    };

    return (
        <section className="bg-blue-50">
            <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
                <a
                    href="#"
                    className="flex items-center mb-6 text-2xl font-semibold text-green-700"
                >
                    <img
                        className="w-32 h-32 mr-2 rounded-full"
                        src={logo}
                        alt="logo"
                    />
                    Phòng khám da liễu Minh Phúc
                </a>
                <div className="w-full bg-white rounded-lg shadow md:mt-0 sm:max-w-md xl:p-0">
                    <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
                        <h1 className="text-xl font-bold leading-tight tracking-tight text-green-700 md:text-2xl">
                            Đăng nhập tài khoản bệnh nhân
                        </h1>
                        <form className="space-y-4 md:space-y-6" action="#">
                            <div>
                                <label
                                    htmlFor="email"
                                    className="block mb-2 text-sm font-medium text-green-700">
                                    Tên tài khoản
                                </label>
                                <input
                                    value={data.userName} onChange={(e) => setData({...data, userName: e.target.value})}
                                    type="email"
                                    name="email"
                                    id="email"
                                    className="bg-blue-50 border border-blue-300 text-gray-900 rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                    required=""/>
                            </div>
                            <div>
                                <label
                                    htmlFor="password"
                                    className="block mb-2 text-sm font-medium text-green-700">
                                    Mật khẩu
                                </label>
                                <input
                                    type='password' value={data.passWord}
                                    onChange={(e) => setData({...data, passWord: e.target.value})}
                                    name="password"
                                    id="password"
                                    placeholder="••••••••"
                                    className="bg-blue-50 border border-blue-300 text-gray-900 rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                    required=""
                                />
                            </div>
                            <div>
                                <label
                                    htmlFor="password"
                                    className="block mb-2 text-sm font-medium text-green-700">
                                    Nhập lại mật khẩu
                                </label>
                                <input
                                    type='password' value={data.passWord}
                                    onChange={(e) => setData({...data, passWord: e.target.value})}
                                    name="password"
                                    id="password"
                                    placeholder="••••••••"
                                    className="bg-blue-50 border border-blue-300 text-gray-900 rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                    required=""
                                />
                            </div>

                            <div>
                                <label
                                    htmlFor="password"
                                    className="block mb-2 text-sm font-medium text-green-700">
                                    Số điện thoại
                                </label>
                                <input
                                    type='password' value={data.passWord}
                                    onChange={(e) => setData({...data, passWord: e.target.value})}
                                    name="password"
                                    id="password"
                                    placeholder="••••••••"
                                    className="bg-blue-50 border border-blue-300 text-gray-900 rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                    required=""
                                />
                            </div>

                            <div>
                                <label
                                    htmlFor="password"
                                    className="block mb-2 text-sm font-medium text-green-700">
                                    Địa chỉ email
                                </label>
                                <input
                                    type='password' value={data.passWord}
                                    onChange={(e) => setData({...data, passWord: e.target.value})}
                                    name="password"
                                    id="password"
                                    placeholder="••••••••"
                                    className="bg-blue-50 border border-blue-300 text-gray-900 rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                    required=""
                                />
                            </div>
                            <p className="text-red-500 text-sm font-medium">{labelError}</p>
                            <div className="flex items-center justify-between">
                                <p>
                                    <Link
                                        to={RoutesConstant.PATIENT.LOGIN}
                                        className="text-sm font-medium text-blue-600 hover:underline">
                                        Quay lại trang đăng nhập
                                    </Link>
                                </p>
                            </div>
                            <button
                                onClick={() => {
                                    setToast({message: "Đăng ký tài khoản thành công", type: 'oke'})
                                    setTimeout(() => {
                                        window.location.reload();
                                    }, 1000)
                                }}
                                type="button"
                                className="w-full text-white bg-green-600 hover:bg-green-700 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center">
                                Đăng ký tài khoản
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            {toast && <ToastPopup message={toast.message}
                                  type={toast.type}
                                  onClose={hideToast}/>}
        </section>)
}

export {SignUp}