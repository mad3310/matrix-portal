/**
 * 
 */

function pageControl() {
	// 首页
	$("#firstPage").bind("click", function() {
		currentPage = 1;
		queryByPage(currentPage);
	});

	// 上一页
	$("#prevPage").click(function() {
		if (currentPage == 1) {
			alert("已经到达第一页");
			return;
		} else {
			currentPage--;
			queryByPage(currentPage);
		}
	});

	// 下一页
	$("#nextPage").click(function() {
		if (currentPage == $("#totalPage_input").val()) {
			alert("已经到达最后一页");
			return;
		} else {
			currentPage++;
			queryByPage(currentPage);
		}
	});

	// 末页
	$("#lastPage").bind("click", function() {
		currentPage = $("#totalPage_input").val();
		queryByPage(currentPage);
	});
	$("#searchButton").click(function() {
		queryByPage(currentPage);
	});
}
function queryByPage(){};