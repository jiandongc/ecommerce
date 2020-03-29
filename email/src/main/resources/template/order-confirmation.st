order-confirmation(customerName, orderNumber, orderEta, orderDeliveryMethod, shippingAddress, orderItems) ::= <<
<!DOCTYPE html>
<html>
<head>
</head>
<body style="background-color: #f6f6f6;">

<div class="container" style="background-color: #ffffff; max-width: 600px; margin: 0 auto; overflow: hidden; border-radius: 5px; font-family: sans-serif;">
    <div class="header" style="background-color: #f6f6f6; padding-bottom: 10px; padding-top: 10px;">
        <img src="https://lh3.googleusercontent.com/n7geLROEAZMDDkCRSN7ztgze9D-hAIznjkAuiTrYGKbsGitkSxjn5xn_0k-1rxLpU7idXBi7OkLgo0w_U1SSqyF5-Q8jp2n00E5-sIrhLveUussQ-95uxga2r7cCkpiZ4YCT11nMQr7foEEjWhwYgzcADS6l4MTozH4xtc8GCYuxzj28nchjZKXvuWjpH5FdE6VVDXSHKO2hdIgjsqCuY6LwRJ55ToEUCYRf3lN_WprmruQTE6Q3vKpkWN-RK273Ua8S4TD57oXXesnvF4p-jfAXZnS6n9DrTPObLjlZ_UYlxGR6kQu8JznAYVEmSBOyceaSGs5SNcTkrHqgplq3wBG-7I8HazKC5uwsmkOJv9khvmEQFFffF2p-fC3c1zSfLRt8RcFGJ2iZxRmejvk_LYRCNyx8viqJd3GTxrHn93BF2n1EbfHwJRXu7CNJzTgqc7MGHIGYVzS7MK19JChnF5rKLXWAd-BNZRdbBDxuGhSWl4fB1buRymSX0JesCEcY9DTsOgjP3P3-YdLKwlGN_TKL0EHWOXCWUVMMSWg-9ohFjVifgr44u2oA7b3FBf6_DAAaHZu0Yse16aeFnrTlAcbIGVINHC-Hdn-_BEBOVqLhXtv4YOTDKpQ5FKBUwGSfJ0j3A0W8ZCp_ounn6solkARQKKafYXZMddfNixyBjtaqZug9WKuZ7Q=w1000-h647-no" style="max-height: 40px;">
    </div>
    <div class="message" style="margin: 20px; line-height: 1.5;">
        <div style="overflow: auto; color: #333; font-size: 14px;">
            <div style="float: right;">
                <div style="font-size: 1.3em; font-weight: bold; float: right;">Order Confirmation</div>
                <div style="font-weight: 300;">Order #<a href="">$orderNumber$</a></div>
            </div>
        </div>

        <div style="font-size: 14px; margin-bottom: 0.5em;"><span style="color:#f0c14b;">Hello,</span> <a href="">$customerName$</a></div>
        <div style="font-size: 14px; font-weight: bold; margin-bottom: 0.5em;">
            Thanks for your order. We'll let you know once your item(s) have dispatched. Your estimated delivery date is indicated below. You can view the status of your order by visiting Your Orders on <a href="">Cawaii.co.uk</a>.
        </div>

        <div style="background-color: #E8E8E8; margin-bottom: 0.5em;">
            <table style="width:100%; font-size: 14px;">
              <tr>
                <td style="padding-left: 10px; width: 50%">
                    <div style="color:#505050;">Arriving:</div>
                    <div style="color:#009933; font-weight: bold; margin-bottom: 0.5em;">$orderEta$</div>
                </td>
                <td rowspan="2" style="vertical-align: top; padding-left: 10px; width: 50%">
                    <div style="color:#505050;">Your order will be sent to:</div>
                    <div style="font-weight: bold;">$shippingAddress.title$ $shippingAddress.name$</div>
                    <div style="font-weight: bold;">$shippingAddress.addressLine1$</div>
                    <div style="font-weight: bold;">$shippingAddress.addressLine2$</div>
                    <div style="font-weight: bold;">$shippingAddress.city$, $shippingAddress.postcode$</div>
                    <div style="font-weight: bold;">$shippingAddress.country$</div>
                </td>
              </tr>
              <tr>
                <td style="vertical-align: top; padding-left: 10px; width: 50%">
                    <div style="color:#505050;">Your delivery option:</div>
                    <div style="font-weight: bold; margin-bottom: 0.5em;">$orderDeliveryMethod$</div>
                </td>
              </tr>
            </table>
        </div>

        <div style="color: #f0c14b; font-weight: bold; font-size: 14px; border-bottom: 2px solid #E8E8E8; width: 100%; display: block; margin-bottom: 0.5em;">
            Order Details
        </div>

        <div style="margin-bottom: 0.5em;">
            <table style="width:100%; font-size: 14px;">

            $orderItems :{ orderItem |
              <tr style="margin-bottom: 0.5em;">
                <td width="30%">
                    <div class="col-xs-6">
                        <a href="#!/products/$orderItem.code$"><img style="display: block; padding: 4px; background-color: #fff; border: 1px solid #ddd; border-radius: 4px; vertical-align: middle; max-width: 100%; height: auto;" src="/Users/jiandongchen/Projects/ecommerce/angular-app/images/1017004481.webp"></a>
                    </div>
                </td>
                <td style="vertical-align: top;" width="70%">
                    <div style="padding-left: 30px;">
                        <a href="#!/products/$orderItem.code$"><span style="color: #4c4445; font-size: 1.0em; font-weight: bold;">$orderItem.name$</span></a>
                    </div>
                    <div style="padding-left: 30px;">
                        <span style="font-weight: 200;">#$orderItem.sku$</span>
                    </div>
                    <div style="padding-left: 30px;">
                        <span style="font-weight: 300;">$orderItem.description$</span>
                    </div>
                    <div style="padding-left: 30px;">
                        <span style="font-weight: 200;">Price (each): </span>£ $orderItem.price$
                    </div>
                    <div style="padding-left: 30px;">
                        <span style="font-weight: 200;">Quantity: </span>$orderItem.quantity$
                    </div>
                    <div style="padding-left: 30px;">
                        <span style="font-weight: 200;">Sub-total: </span>£ $orderItem.subTotal$
                    </div>
                </td>
              </tr>
              }$

            </table>
        </div>

    </div>
    <div class="footer" style="padding-top: 30px; padding-bottom: 15px; background-color: #f6f6f6; text-align: center; color: grey; font-size: 12px; letter-spacing: 0.8px;">
        @ 2019 CAWAII. INC.
    </div>
</div>

</body>
</html>
>>