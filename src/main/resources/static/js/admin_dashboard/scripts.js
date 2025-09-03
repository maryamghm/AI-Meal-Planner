$('.navTrigger').click(function () {
    $(this).toggleClass('active');
    $("#mainListDiv").toggleClass("show_list");

    $("body").toggleClass("no-scroll");

    let expanded = $(this).attr("aria-expanded") === "true" || false;
    $(this).attr("aria-expanded", !expanded);
});

$("#mainListDiv ul li a").click(function () {
    $(".navTrigger").removeClass("active").attr("aria-expanded", "false");
    $("#mainListDiv").removeClass("show_list");
    $("body").removeClass("no-scroll");
});