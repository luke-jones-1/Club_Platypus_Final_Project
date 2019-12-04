//emoji keyboard
  // jquery
  $(document).ready(function(){
    $(".emojiFrame").click(function(){
      // sets target as message box
      var target = $("#message");
      // stores current text
      var temp = target.val()
       // adds emoji and current text
      $('#message').val(temp + $(this).text());
    })
  })

