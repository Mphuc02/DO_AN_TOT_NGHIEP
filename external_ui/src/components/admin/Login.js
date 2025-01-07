import axios from 'axios';
import {AUTHENTICATION, EMPLOYYEE} from "../../ApiConstant";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import logo from "../../imgs/logo.jpg";
import RoutesConstant from "../../RoutesConstant";
import {SendApiService} from "../../service/SendApiService";

function Login(){
    const [userName, setUserName] = useState('')
    const [passWord, setPassWord] = useState('')
    const [labelError, setLabelError] = useState('')
    const navigate = useNavigate();

    const getEmployeeInformation = () => {
        return new Promise((resolve, reject) => {
            SendApiService.getRequest(EMPLOYYEE.getLoggedUserInformation(), {}, response => {
                localStorage.setItem('admin', JSON.stringify(response.data))
                resolve()
            }, error => {
                reject()
            })
        })
    }

    function doLogin(){
        const data = {
            userName: userName,
            passWord: passWord
        }
        console.log(data)

        axios.post(AUTHENTICATION.authenticateEmployee(), data)
            .then(async response => {
                console.log(response.data);
                localStorage.setItem('access-token', response.data.accessToken)
                localStorage.setItem('refresh-token', response.data.refreshToken)

                await getEmployeeInformation()

                navigate(RoutesConstant.ADMIN.EMPLOYEE_MANAGEMENT)
            })
            .catch(error => {
                console.log(error.response);
                if(error.response != null)
                    setLabelError('Tài khoản hoặc mật khẩu không chính xác')
            });
    }

    return(
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
                            Đăng nhập bằng tài khoản quản trị viên
                        </h1>
                        <form className="space-y-4 md:space-y-6" action="#">
                            <div>
                                <label
                                    htmlFor="email"
                                    className="block mb-2 text-sm font-medium text-green-700"
                                >
                                    Email của bạn
                                </label>
                                <input
                                    value={userName} onChange={(e) => setUserName(e.target.value)}
                                    type="email"
                                    name="email"
                                    id="email"
                                    className="bg-blue-50 border border-blue-300 text-gray-900 rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                    required=""
                                />
                            </div>
                            <div>
                                <label
                                    htmlFor="password"
                                    className="block mb-2 text-sm font-medium text-green-700">
                                    Mật khẩu
                                </label>
                                <input
                                    type='password' value={passWord} onChange={(e) => setPassWord(e.target.value)}
                                    name="password"
                                    id="password"
                                    placeholder="••••••••"
                                    className="bg-blue-50 border border-blue-300 text-gray-900 rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                    required=""
                                />
                            </div>
                            <p className="text-red-500 text-sm font-medium">{labelError}</p>
                            <div className="flex items-center justify-between">
                                <a
                                    href="#"
                                    className="text-sm font-medium text-blue-600 hover:underline">
                                    Quên mật khẩu?
                                </a>
                            </div>
                            <button
                                onClick={doLogin}
                                type="button"
                                className="w-full text-white bg-green-600 hover:bg-green-700 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center">
                                Đăng nhập
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    )
}

export default Login