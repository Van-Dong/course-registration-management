# Course Project Manager
Ứng dụng "Quản lý Đăng ký Khóa học" cho phép sinh viên đăng ký, xem, và quản lý các khóa học của mình. 
Quản trị viên có thể quản lý danh sách khóa học và xem báo cáo thống kê số lượng đăng ký hàng tuần. 
Ứng dụng sẽ có một chức năng chạy batch định kỳ để tạo báo cáo và gửi thông báo nhắc nhở đăng ký qua email cho sinh viên.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)

## Installation
0. Requirements
  - Docker
1. Clone the repository
   ```bash
   git clone https://github.com/Van-Dong/course-registration-management
2. Navigate into the project directory
   ```bash
   cd course-registration-management
3. Edit the `docker-compose.yml` file to set your environment variables as needed
4. Start the application. Note: Default account admin is "admin:admin"
   ```bash
   docker compose up
5. Stop the application
   ```
   docker compose down