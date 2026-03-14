/* Script slider */
const slides = document.querySelectorAll("#slider .slide");
let index = 0;
/* Kiểm tra trong code html có #slider mới active */
if (slides.length > 0) {
    slides[index].classList.add("active");

    function nextSlide() {
        slides[index].classList.remove("active");
        index = (index + 1) % slides.length;
        slides[index].classList.add("active");
    };

    slides[index].classList.add("active");
    setInterval(nextSlide, 3000);
}

// Script Navigation button 
document.querySelectorAll('.featured').forEach(section => {
    const list = section.querySelector('.product-list');
    const btnLeft = section.querySelector('.scroll-btn.left');
    const btnRight = section.querySelector('.scroll-btn.right');
    if (btnLeft && btnRight && list) {
        btnLeft.addEventListener('click', () => {
            list.scrollBy({ left: -300, behavior: 'smooth' });
        });

        btnRight.addEventListener('click', () => {
            list.scrollBy({ left: 300, behavior: 'smooth' });
        });
    }
});

//Click button like
const favBtns = document.querySelectorAll('.fav-btn');
    if (favBtns.length > 0) {
        favBtns.forEach(favBtn => {
            favBtn.addEventListener('click', () => {
                favBtn.classList.toggle('active');
                const heartIcon = favBtn.querySelector('i');

                if (favBtn.classList.contains('active')) {
                    heartIcon.classList.remove('fa-regular');
                    heartIcon.classList.add('fa-solid');
                } else {
                    heartIcon.classList.remove('fa-solid');
                    heartIcon.classList.add('fa-regular');
                }
            });
        });
    }

//Click button view
const slideWrappers = document.querySelectorAll('.slide-wrapper');

slideWrappers.forEach(wrapper => {
    const button = wrapper.querySelector('.button-view');
    const icon = button.querySelector('i');
    
    button.addEventListener('click', () => {
        wrapper.classList.toggle('active');

        if(wrapper.classList.contains('active')) {
            icon.classList.remove('fa-chevron-down');
            icon.classList.add('fa-chevron-up');
        } else {
            icon.classList.remove('fa-chevron-up');
            icon.classList.add('fa-chevron-down');
        }
    });
});