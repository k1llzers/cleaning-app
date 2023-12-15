const logoEl = document.querySelector('.logo');

// check header height
function checkHeaderHeight() {
    // select header element
    const header = document.querySelector('header');
    // get rendered styles
    const styles = window.getComputedStyle(header);
    // set header height rendered style
    const headerHeight = styles.height;
    // set CSS as a value
    document.documentElement.style.setProperty("--sl-header-height", headerHeight);
}

addEventListener("resize", checkHeaderHeight);
addEventListener("orientationchange", checkHeaderHeight);
checkHeaderHeight();
logoEl.addEventListener('click', () => {
    window.location.replace('/');
});

var link = document.querySelector("link[rel~='icon']");
if (!link) {
    link = document.createElement('link');
    link.rel = 'icon';
    document.head.appendChild(link);
}
if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    console.log('dark mode');
    link.href = 'images/favicon_light.ico';
} else {
    console.log('light mode');
    link.href = 'images/favicon_dark.ico';
}
