  // Create Toast message
  function createToast(type, message) {
    if (type == "info") {
      $("#info_toast div.toast-body").html(message);

      $("#info_toast").addClass("show");

      setTimeout(function () {
        $("#info_toast").removeClass("show");
      }, 5000);
    } else if (type == "warning") {
      $("#warning_toast div.toast-body").html(message);

      $("#warning_toast").addClass("show");

      setTimeout(function () {
        $("#warning_toast").removeClass("show");
      }, 5000);
    } else if (type == "danger") {
      $("#danger_toast div.toast-body").html(message);

      $("#danger_toast").addClass("show");

      setTimeout(function () {
        $("#danger_toast").removeClass("show");
      }, 5000);
    }
  }