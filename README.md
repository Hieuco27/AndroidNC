🏠 Ứng Dụng Quản Lý Nhà Trọ
📖 Giới thiệu
Ứng dụng Quản Lý Nhà Trọ là một dự án bài tập lớn phát triển trên nền tảng Android, hỗ trợ chủ nhà trọ quản lý phòng trọ, khách thuê và tài chính một cách hiệu quả. Với giao diện thân thiện, ứng dụng giúp tiết kiệm thời gian và giảm thiểu sai sót trong quản lý.
Dự án sử dụng Java và SQLite (Firebase) để lưu trữ dữ liệu.
✨ Tính năng chính

🛋️ Quản lý phòng trọ:
Thêm, xóa, sửa thông tin phòng (giá thuê, trạng thái: trống/đã thuê).
Hiển thị danh sách phòng trực quan.


👤 Quản lý khách thuê:
Lưu thông tin khách (họ tên, số điện thoại, CMND/CCCD).
Liên kết khách với phòng trọ.

📝 Quản lý hợp đồng:
Lưu thông tin hợp đồng (ngày bắt đầu, kết thúc, điều khoản).
Cảnh báo hợp đồng sắp hết hạn.

🧾 Quản lý hóa đơn:
Tự động tính tiền thuê, điện, nước, dịch vụ.
Tạo và lưu trữ hóa đơn hàng tháng.
Xem lịch sử hóa đơn theo phòng/khách.


📬 Thông báo:
Gửi nhắc nhở thanh toán qua app, email hoặc Zalo.
Thông báo phòng trống cho chủ trọ.


📊 Báo cáo thu chi:
Thống kê doanh thu, chi phí, lợi nhuận theo tháng/năm.
Xuất báo cáo dạng bảng hoặc biểu đồ.


// PHÁT TRIỂN THÊM Ở TƯƠNG LAI

🌐 Đăng ký tạm trú online:
Gửi thông tin khách thuê lên dịch vụ công.
Lưu trữ thông tin để tra cứu.


🛠️ Công nghệ sử dụng

Ngôn ngữ lập trình: Java
Cơ sở dữ liệu: SQLite (Firebase)
Giao diện: Material Design, RecyclerView, Fragment
Thư viện:
Room Database
Firebase Cloud Messaging
Retrofit (cho API)


Công cụ: Android Studio, Git

🚀 Cài đặt và chạy ứng dụng
📋 Yêu cầu

Android Studio
Thiết bị Android API 21+ hoặc emulator
Kết nối internet (nếu dùng Firebase/API)

📦 Hướng dẫn cài đặt

Clone repository:git clone https://github.com/<Hieuco27>/<AndroidNC>.git


Mở dự án:
Mở Android Studio, chọn File > Open, chọn thư mục dự án.


Cài dependencies:
Nhấn Sync Project with Gradle Files.


Cấu hình cơ sở dữ liệu:
SQLite: Không cần cấu hình.
Firebase: Thêm file google-services.json vào thư mục app/.


Chạy ứng dụng:
Kết nối thiết bị hoặc dùng emulator, nhấn Run.



🎮 Cách sử dụng

Đăng nhập: Dùng tài khoản chủ trọ .
Quản lý phòng: Thêm/sửa/xóa phòng, cập nhật trạng thái.
Quản lý khách: Thêm thông tin khách và liên kết phòng.
Hợp đồng/tạm trú: Tạo hợp đồng, gửi thông tin tạm trú.
Tạo hóa đơn: Nhập chỉ số điện/nước, hệ thống tạo hóa đơn.
Xem báo cáo: Kiểm tra thu chi trong mục "Báo cáo".


📁 Cấu trúc dự án

├── app
│   ├── src
│   │   ├── main
│   │   │   ├── java/com/example/navigationfragment
│   │   │   │   ├── activity      # 🎨 MainActivity, RoomActivity,...
                ├── action        # AddRoom, AddKhach ,.....
│   │   │   │   ├── adapter       # 🔄 RecyclerView Adapter        
│   │   │   │   ├── entity        # 🗂️ Room, Tenant, Invoice,...
│   │   │   │   ├── DAO           # 💾 RoomDao, DAO
│   │   │   │   ├── fragment     # 📑 PhongFragment, KhachFragment,...
│   │   │   ├── res
│   │   │   │   ├── layout       # 🖼️ XML giao diện
│   │   │   │   ├── values       # 🎨 Theme, string, color
│   ├── build.gradle             # ⚙️ Dependencies
├── README.md                    # 📜 File này

🔮 Kế hoạch phát triển

Hiện tại: Quản lý cơ bản (phòng, khách, hóa đơn).
Tương lai:

🖨️ Xuất hóa đơn PDF.
💳 Tích hợp thanh toán online (Momo, ZaloPay).
🌍 Hỗ trợ đa ngôn ngữ.
⚡ Tối ưu hiệu suất.



🤝 Đóng góp

Fork repository.
Tạo branch: git checkout -b feature/<tên-tính-năng>.
Commit: git commit -m "Mô tả thay đổi".
Push: git push origin feature/<tên-tính-năng>.
Tạo Pull Request.

📧 Liên hệ

Email: thaivanhieu2710@gmail.com
GitHub Issues: Tạo issue

📜 Giấy phép
MIT License

🙏 Cảm ơn bạn đã quan tâm đến Ứng Dụng Quản Lý Nhà Trọ!
