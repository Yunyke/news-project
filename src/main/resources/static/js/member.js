document.addEventListener("DOMContentLoaded", async function() {
	if (!window.IS_LOGGED_IN) {
		localStorage.removeItem("newsCart");
	}

	const urlParams = new URLSearchParams(window.location.search);
	if (urlParams.get("resetCart") === "true") {
		localStorage.removeItem("newsCart");
		const cleanUrl = window.location.origin + window.location.pathname;
		window.history.replaceState({}, document.title, cleanUrl);
	}

	let cart = JSON.parse(localStorage.getItem("newsCart") || "[]");


	if (window.IS_LOGGED_IN) {
		try {
			const res = await fetch(`/api/cart/users/${window.USER_SESSION.userId}/cart`);
			if (res.ok) {
				const serverIds = await res.json();
				cart = Array.from(new Set([...cart, ...serverIds]));
				localStorage.setItem("newsCart", JSON.stringify(cart));
			}
		} catch (err) {
			console.error("雲端清單讀取失敗：", err);
		}
	}

	document.querySelectorAll(".add-to-cart-btn").forEach(btn => {
		const id = parseInt(btn.dataset.id, 10);
		const card = btn.closest(".news-card");


		if (cart.includes(id)) {
			card.classList.add("added-to-cart");
			btn.innerText = "✅ 已加入";
		}


		btn.addEventListener("click", function(event) {
			event.stopPropagation();

			/* 未登入 */
			if (!window.IS_LOGGED_IN) {
				alert("請先登入才能加入新聞清單！");
				return;
			}
			const userId = window.USER_SESSION.userId;
			if (!userId) {
				alert("無法辨識使用者 ID，請重新登入");
				return;
			}

			/* --- 加入 --- */
			if (!cart.includes(id)) {
				cart.push(id);
				card.classList.add("added-to-cart");
				btn.innerText = "✅ 已加入";

				fetch(`/api/cart/users/${userId}/cart`, {
					method: "POST",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify({ newsId: id })
				}).catch(err => console.error("加入清單失敗：", err));

				/* --- 移除 --- */
			} else {
				cart = cart.filter(itemId => itemId !== id);
				card.classList.remove("added-to-cart");
				btn.innerText = "➕";

				fetch(
					`/api/cart/users/${userId}/cart/${id}`,
					{ method: "DELETE" }
				).catch(err => console.error("移除清單失敗：", err));
			}

			localStorage.setItem("newsCart", JSON.stringify(cart));
			updateCartCount();
		});
	});

	updateCartCount();

});

/* ----------更新數字 ---------- */
function updateCartCount() {
	const cart = JSON.parse(localStorage.getItem("newsCart") || "[]");
	const badge = document.getElementById("cart-count");
	if (badge) {
		badge.innerText = cart.length;
		badge.style.display = cart.length === 0 ? "none" : "inline-block";
	}
}
document.getElementById("logoutBtn")?.addEventListener("click", function(e) {
	e.preventDefault(); // 防止預設跳轉

	// 清除本地儲存
	console.log("🚪 使用者登出，清除 localStorage.newsCart");
	localStorage.removeItem("newsCart");
	updateCartCount();
	// 導向後端 logout URL
	window.location.href = "/logout";
});

/* ---------- 查看新聞清單 ---------- */
function handleViewCartClick() {
	if (!window.IS_LOGGED_IN) {
		alert("請先登入才能查看新聞清單！");
		return;
	}
	window.location.href = "/cart";
}