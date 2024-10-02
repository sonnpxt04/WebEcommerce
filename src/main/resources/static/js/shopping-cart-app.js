const app = angular.module("shopping-cart-app", []);

app.controller("shopping-cart-ctrl", function($scope, $http) {
    $scope.cart = {
        items: [],

        add(id) {
            var item = this.items.find(item => item.id == id);
            if (item) {
                item.qty++;
                this.saveToLocalStorage();
            } else {
                $http.get(`/rest/products/${id}`).then(resp => {
                    resp.data.qty = 1;
                    this.items.push(resp.data);
                    this.saveToLocalStorage();
                });
            }
        },
        getTotal: function() {
            let total = 0;
            for (let item of this.items) {
                total += item.qty * item.price;
            }
            return total;
        },
        remove(id) {
            var itemIndex = this.items.findIndex(item => item.id == id);
            if (itemIndex !== -1) {
                this.items.splice(itemIndex, 1);
                this.saveToLocalStorage();
            }
        },

        clear() {
            this.items = [];
            this.saveToLocalStorage();
        },

        amt_of(item) {
            // Implement if needed
        },

        get count() {
            return this.items
                .map(item => item.qty)
                .reduce((total, qty) => total += qty, 0);
        },

        get amount() {
            return this.items
                .map(item => item.qty * item.price)
                .reduce((total, amount) => total += amount, 0);
        },

        saveToLocalStorage() {
            var json = JSON.stringify(angular.copy(this.items));
            localStorage.setItem("cart", json);
        },

        loadFromLocalStorage() {
            var json = localStorage.getItem("cart");
            this.items = json ? JSON.parse(json) : [];
        }
    };

    $scope.cart.loadFromLocalStorage();

    $scope.order = {
        createDate: new Date(),
        address: "",
        account: { username: "" },

        updateAccountUsername() {
            this.account.username = document.getElementById("username")?.textContent.trim() || "";
        },

        orderDetails() {
            return $scope.cart.items.map(item => ({
                productId: item.id,
                price: item.price,
                quantity: item.qty
            }));
        },

        purchase() {
            this.updateAccountUsername();

            var order = {
                createDate: this.createDate,
                address: this.address,
                account: this.account,
                orderDetails: this.orderDetails()
            };

            $http.post("/api/payment/create_payment", order).then(resp => {
                console.log("Phản hồi từ máy chủ:", resp); // In toàn bộ phản hồi để kiểm tra

                if (resp.data && resp.data.url) {
                    console.log("URL thanh toán:", resp.data.url); // In URL ra console

                    Swal.fire({
                        title: "Đặt hàng thành  công!",
                        text: "Nhấp vào liên kết để thanh toán:",
                        html: `<a href="${resp.data.url}" target="_blank">Thanh toán tại đây</a>`,
                        icon: "info",
                        confirmButtonText: "Đóng"
                    });
                } else {
                    Swal.fire("Thông báo", "URL thanh toán không hợp lệ", "error");
                }
            }).catch(error => {
                console.log("Lỗi khi gửi yêu cầu:", error); // In lỗi nếu có
                Swal.fire("Thông báo", "Đặt hàng lỗi", "error");
            });
        }



    };

});
document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const paymentStatus = params.get('paymentStatus');
    const message = params.get('message');

    if (paymentStatus && message) {
        Swal.fire({
            title: paymentStatus === 'success' ? 'Thanh toán thành công!' : 'Thanh toán thất bại!',
            text: message,
            icon: paymentStatus === 'success' ? 'success' : 'error',
            confirmButtonText: 'Đóng'
        });
    }
});