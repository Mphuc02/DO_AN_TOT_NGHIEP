
const GetTodayString = () => {
    const timeZone = 'Asia/Ho_Chi_Minh'; // Thay bằng timezone bạn muốn

    // Lấy ngày hiện tại theo timezone cụ thể
    const today = new Date().toLocaleString('en-CA', {
        timeZone: timeZone,
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });

    // Chuyển đổi format từ "YYYY-MM-DD" và không chứa giờ phút
    const todayTime = today.split(',')[0]; // Kết quả đã là "YYYY-MM-DD"
    return today
}

export {GetTodayString}