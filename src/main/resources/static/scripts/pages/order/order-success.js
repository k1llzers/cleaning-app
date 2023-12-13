if(sessionStorage.getItem('order') !== 'success') {
    window.location.replace('/');
}
sessionStorage.removeItem('order');
