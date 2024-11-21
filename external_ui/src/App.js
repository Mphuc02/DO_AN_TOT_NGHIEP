import {BrowserRouter as Router, Routes, Route} from 'react-router-dom'
import {publicRoutes} from "./routes";
import SockJS from "sockjs-client";
import {WEBSOCKET} from "./ApiConstant";
import {Stomp} from "@stomp/stompjs";

const test = () => {
    const socket = new SockJS(WEBSOCKET.getUrl());
    const stompClient = Stomp.over(socket);

    // Kết nối STOMP
    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);

        // Đăng ký vào topic để nhận tin nhắn từ server
        stompClient.subscribe("/topic/chat/user/058fd7dc-bcd1-4a76-87d4-1f942d865d35", (message) => {
            console.log('Received message: ', message);
        });

        // Sau khi kết nối thành công, gửi tin nhắn tới server
        stompClient.send('/topic/chat/user/058fd7dc-bcd1-4a76-87d4-1f942d865d35', {}, JSON.stringify('hello'));
    });
}

function App() {
    test()

    return (
        <Router>
            <div className="App">
                <Routes>
                    {publicRoutes.map((route, index) => {
                        const Layout = route.layout
                        const Page = route.component
                        return <Route key={index} path={route.path} element={<Layout>
                            <Page/>
                        </Layout>} />
                    })}
                </Routes>
            </div>
        </Router>
    );
}

export default App;
