<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tìm kiếm sản phẩm</title>

    <!-- Link Reset CSS -->
    <link rel="stylesheet" href="assets/css/reset.css">
    <!-- Link font -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
        href="https://fonts.googleapis.com/css2?family=Open+Sans:ital,wght@0,300..800;1,300..800&family=Poppins:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
        rel="stylesheet">
    <!-- Link icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"
        integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw=="
        crossorigin="anonymous" referrerpolicy="no-referrer" />
    <!-- Link CSS -->
    <link rel="stylesheet" href="assets/css/grid.css">
    <link rel="stylesheet" href="assets/css/base.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/search.css?v=1">

    <!-- Link favicon -->
</head>

<body>
    <!-- header -->
    <%@ include file="/common/header.jsp" %>

    <main class="main" style="height: 5000px; background-color: #d9d9d9">
        <div class="search-filter">
            <div class="grid wide">
                <div class="row small-gutter">
                    <div class="col l-2 m-0 c-0">
                        <section class="search-filter__inner">
                            <nav class="category">
                                <h2 class="category__heading">Danh mục</h2>
                                <ul class="category__list">
                                    <li class="category__item category__item--active">
                                        <a href="#!" class="category__link">Gia dụng - Nhà cửa</a>
                                        <ul class="category-menu">
                                            <li class="category-menu__item">
                                                <a href="#!" class="category-menu__link">Đồ dùng nhà bếp</a>
                                            </li>
                                            <li class="category-menu__item">
                                                <a href="#!" class="category-menu__link">Dụng cụ làm vườn</a>
                                            </li>
                                            <li class="category-menu__item">
                                                <a href="#!" class="category-menu__link">Đồ dùng sinh hoạt</a>
                                            </li>
                                            <li class="category-menu__item">
                                                <a href="#!" class="category-menu__link">Vệ sinh nhà cửa</a>
                                            </li>
                                            <li class="category-menu__item">
                                                <a href="#!" class="category-menu__link">Dụng cụ sửa chữa</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li class="category__item">
                                        <a href="#!" class="category__link">Phụ kiện ô tô</a>
                                    </li>
                                    <li class="category__item">
                                        <a href="#!" class="category__link">Thời trang</a>
                                    </li>
                                    <li class="category__item">
                                        <a href="#!" class="category__link">Âm thanh - Camera</a>
                                    </li>
                                </ul>
                            </nav>

                            <div class="search-filter__header">
                                <i class="search-filter__icon fa-solid fa-filter"></i>
                                <h2 class="search-filter__heading">Bộ lọc tìm kiếm</h2>
                            </div>

                            <article class="search-filter__category">
                                <h3 class="search-filter__title">Theo thương hiệu</h3>
                                <div class="search-filter__options">

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="brands" class="filter-checkbox" id="brand-spark-l" value="SPARK-L" />
                                        <label class="search-filter__checkbox" for="brand-spark-l">SPARK-L</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="brands" class="filter-checkbox" id="brand-oboking" value="Obo king" />
                                        <label class="search-filter__checkbox" for="brand-oboking">Obo king</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="brands" class="filter-checkbox" id="brand-gardena" value="Gardena" />
                                        <label class="search-filter__checkbox" for="brand-gardena">Gardena</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="brands" class="filter-checkbox" id="brand-worx" value="WORX" />
                                        <label class="search-filter__checkbox" for="brand-worx">WORX</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="brands" class="filter-checkbox" id="brand-black-decker" value="Black+Decker" />
                                        <label class="search-filter__checkbox" for="brand-black-decker">Black+Decker</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="brands" class="filter-checkbox" id="brand-okabe" value="Okabe" />
                                        <label class="search-filter__checkbox" for="brand-okabe">Okabe</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="brands" class="filter-checkbox" id="brand-anhvica" value="Anh Vi Cá" />
                                        <label class="search-filter__checkbox" for="brand-anhvica">Anh Vi Cá</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="brands" class="filter-checkbox" id="brand-mayo" value="Mayo" />
                                        <label class="search-filter__checkbox" for="brand-mayo">Mayo</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="brands" class="filter-checkbox" id="brand-kobayashi" value="Kobayashi" />
                                        <label class="search-filter__checkbox" for="brand-kobayashi">Kobayashi</label>
                                    </div>

                                </div>
                            </article>

                            <article class="search-filter__category">
                                <h3 class="search-filter__title">Khoảng giá</h3>
                                <div class="search-filter__options">
                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" id="price-all" value="all" />
                                        <label class="search-filter__checkbox" for="price-all">Tất cả</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="priceRanges" class="filter-checkbox" id="price-under100" value="0-100000" />
                                        <label class="search-filter__checkbox" for="price-under100">Dưới 100k</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="priceRanges" class="filter-checkbox" id="price-100-200" value="100000-200000" />
                                        <label class="search-filter__checkbox" for="price-100-200">100k - 200k</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="priceRanges" class="filter-checkbox" id="price-200-500" value="200000-500000" />
                                        <label class="search-filter__checkbox" for="price-200-500">200k - 500k</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="priceRanges" class="filter-checkbox" id="price-500-1000" value="500000-1000000" />
                                        <label class="search-filter__checkbox" for="price-500-1000">500k - 1 triệu</label>
                                    </div>

                                    <div class="search-filter__item">
                                        <input hidden type="checkbox" name="priceRanges" class="filter-checkbox" id="price-over1000" value="1000000-999999999" />
                                        <label class="search-filter__checkbox" for="price-over1000">Trên 1 triệu</label>
                                    </div>
                                </div>
                            </article>
                            <article class="search-filter__category">
                                <h3 class="search-filter__title">Đánh giá</h3>

                                <div class="search-filter-reviews">
                                    <input type="radio" name="rating" class="search-filter-reviews__input" checked />
                                    <div class="search-filter-reviews__content">
                                        <div class="rating">
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                        </div>
                                    </div>
                                </div>

                                <div class="search-filter-reviews">
                                    <input type="radio" name="rating" class="search-filter-reviews__input" />
                                    <div class="search-filter-reviews__content">
                                        <div class="rating">
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star"></i>
                                            <span class="rating__label">Trở lên</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="search-filter-reviews">
                                    <input type="radio" name="rating" class="search-filter-reviews__input" />
                                    <div class="search-filter-reviews__content">
                                        <div class="rating">
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star"></i>
                                            <i class="fa-solid fa-star rating__star"></i>
                                            <span class="rating__label">Trở lên</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="search-filter-reviews">
                                    <input type="radio" name="rating" class="search-filter-reviews__input" />
                                    <div class="search-filter-reviews__content">
                                        <div class="rating">
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star"></i>
                                            <i class="fa-solid fa-star rating__star"></i>
                                            <i class="fa-solid fa-star rating__star"></i>
                                            <span class="rating__label">Trở lên</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="search-filter-reviews">
                                    <input type="radio" name="rating" class="search-filter-reviews__input" />
                                    <div class="search-filter-reviews__content">
                                        <div class="rating">
                                            <i class="fa-solid fa-star rating__star rating__star--gold"></i>
                                            <i class="fa-solid fa-star rating__star"></i>
                                            <i class="fa-solid fa-star rating__star"></i>
                                            <i class="fa-solid fa-star rating__star"></i>
                                            <i class="fa-solid fa-star rating__star"></i>
                                            <span class="rating__label">Trở lên</span>
                                        </div>
                                    </div>
                                </div>
                            </article>
                        </section>
                    </div>

                    <div class="col l-10 m-12 c-12">
                        <section class="product-search-list">
                            <div class="search-header">
                                <c:choose>
                                    <%-- Ưu tiên hiện Keyword nếu đang search --%>
                                    <c:when test="${not empty keyword}">
                                        <p>Kết quả tìm kiếm cho: <span class="word-search">${keyword}</span></p>
                                    </c:when>

                                    <%-- Hiện Tên Danh Mục thật từ Database --%>
                                    <c:when test="${not empty category}">
                                        <p>Danh mục: <span class="word-search">${category.name}</span></p>
                                    </c:when>
                                </c:choose>
                            </div>
                            <div class="row-list row small-gutter" id="content-products">

                                <c:if test="${empty products}">
                                    <p style="padding:20px">Không tìm thấy sản phẩm phù hợp</p>
                                </c:if>

                                <c:forEach items="${products}" var="p">
                                <div class="col l-2-4 m-4 c-6">
                                    <div class="product-card">
                                        <a href="product?id=${p.id}">
                                            <img src="${pageContext.request.contextPath}/assets/img/products/${p.image}" alt="${p.name}">
                                        </a>
                                        <a href="product?id=${p.id}">
                                            <p>${p.name}</p>
                                        </a>

                                        <div class="price-discount">
                                            <c:if test="${p.discountPercent > 0}">
                                                <div class="price-top">
                                                    <span class="old-price"><fmt:formatNumber value="${p.firstPrice}" type="number"/>đ</span>
                                                    <div class="discount-badge">Giảm ${p.discountPercent}%</div>
                                                </div>
                                            </c:if>

                                            <div class="price-bottom">
                                                <span class="new-price"><fmt:formatNumber value="${p.totalPrice}" type="number"/>đ</span>
                                            </div>
                                        </div>
                                        <div class="bottom">
                                            <div class="star"><i class="fa-solid fa-star"></i> ${p.rating}</div>
                                            <button class="fav-btn">
                                                <i class="fa-regular fa-heart"></i> Yêu thích
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                </c:forEach>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </main>
</body>
<!-- Link JS -->
<script src="assets/js/script.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        // 1. Xử lý logic Checkbox
        $(document).on('change', '.filter-checkbox', function() {
            if ($(this).attr('name') === 'priceRanges' && $(this).is(':checked')) {
                $('#price-all').prop('checked', false);
            }
            searchFilter();
        });

        $('#price-all').on('change', function() {
            if ($(this).is(':checked')) {
                $('input[name="priceRanges"]').prop('checked', false);
                searchFilter();
            }
        });

        // 2. Hàm lọc chính
        function searchFilter() {
            // Lấy danh sách Thương hiệu
            let brands = [];
            $('input[name="brands"]:checked').each(function() {
                brands.push($(this).val());
            });

            let priceRanges = [];
            $('input[name="priceRanges"]:checked').each(function() {
                priceRanges.push($(this).val());
            });

            const urlParams = new URLSearchParams(window.location.search);

            let categoryId = urlParams.get('categoryId') || "";


            let keyword = urlParams.get('keyword') || "";

            $.ajax({
                url: "search-product",
                type: "GET",
                data: {
                    keyword: keyword,
                    categoryId: categoryId,
                    'brands[]': brands,
                    'priceRanges[]': priceRanges
                },
                beforeSend: function() {
                    // Hiệu ứng loading
                    $("#content-products").stop(true, true).css("opacity", "0.5");
                },
                success: function(data) {
                    let $htmlResponse = $(data);
                    let newList = $htmlResponse.find("#content-products").html();
                    let newHeader = $htmlResponse.find(".search-header").html();

                    $("#content-products").fadeOut(100, function() {
                        $(this).html(newList).fadeIn(100).css("opacity", "1");
                    });

                    if (newHeader) {
                        $(".search-header").html(newHeader);
                    }
                },
                error: function(xhr) {
                    $("#content-products").css("opacity", "1");
                    console.error("Lỗi AJAX: " + xhr.status + " " + xhr.statusText);
                }
            });
        }
    });
</script>
</html>