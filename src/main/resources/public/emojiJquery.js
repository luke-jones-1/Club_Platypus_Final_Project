//emoji keyboard
  // jquery
  $(document).ready(function(){
    $(".emojiFrame").click(function(){
      // sets target as message box
//          $("#message").append($(this).text());
      var target = $("#message");
  //    // adds the emoji  to end of text box
  //    target.text(target.text() + $(this).text());
      var temp = target.val()
      $('#message').val(temp + $(this).text());
    })
  })

