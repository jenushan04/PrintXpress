#!/usr/bin/env python3
"""Render PrintXpress Java sources as IDE-style PNG screenshots."""
from pathlib import Path
from pygments import highlight
from pygments.lexers import JavaLexer
from pygments.formatters import ImageFormatter

SRC = Path("/Users/jenu/Downloads/PrintXpress/app/src/main/java/com/printxpress/app")
OUT = Path("/Users/jenu/Downloads/PrintXpress/docs/code")
OUT.mkdir(parents=True, exist_ok=True)

# Files to render. Long files chunked into ~60-line images.
files = [
    ("LoginActivity.java",            "login_activity",      None),
    ("RegisterActivity.java",         "register_activity",   None),
    ("SplashActivity.java",           "splash_activity",     None),
    ("CustomerHomeActivity.java",     "customer_home",       None),
    ("ProductListActivity.java",      "product_list",        None),
    ("ProductDetailActivity.java",    "product_detail",      None),
    ("PlaceOrderActivity.java",       "place_order",         (90, 60)),
    ("MyOrdersActivity.java",         "my_orders",           None),
    ("PromotionsActivity.java",       "promotions",          None),
    ("ProfileActivity.java",          "profile",             None),
    ("AdminHomeActivity.java",        "admin_home",          None),
    ("AdminOrdersActivity.java",      "admin_orders",        (80, 60)),
    ("AdminProductsActivity.java",    "admin_products",      None),
    ("AdminEditProductActivity.java", "admin_edit_product",  (80, 60)),
    ("db/DatabaseHelper.java",        "database_helper",     (95, 80)),  # 572 lines
    ("util/SessionManager.java",      "session_manager",     None),
    ("util/Validator.java",           "validator",           None),
    ("util/PasswordUtil.java",        "password_util",       None),
    ("model/User.java",               "model_user",          None),
    ("model/Product.java",            "model_product",       None),
    ("model/Order.java",              "model_order",         None),
    ("model/Promotion.java",          "model_promotion",     None),
]

FORMAT_KWARGS = dict(
    font_name='Menlo',
    font_size=15,
    line_numbers=True,
    line_number_bg='#2b2b2b',
    line_number_fg='#888888',
    line_number_bold=False,
    line_number_separator=False,
    line_pad=4,
    image_pad=14,
    style='monokai',
)

def render(text, out_png):
    formatter = ImageFormatter(**FORMAT_KWARGS)
    png_bytes = highlight(text, JavaLexer(), formatter)
    out_png.write_bytes(png_bytes)
    return out_png

for rel, slug, chunk in files:
    src = SRC / rel
    if not src.exists():
        print(f"MISSING {src}")
        continue
    text = src.read_text()
    if chunk is None:
        render(text, OUT / f"{slug}.png")
        print(f"  {slug}.png")
    else:
        lines = text.splitlines()
        max_lines, overlap_lines = chunk
        parts = []
        i = 0
        while i < len(lines):
            parts.append(lines[i:i+max_lines])
            i += max_lines - 0
        for idx, part in enumerate(parts, 1):
            render("\n".join(part), OUT / f"{slug}_p{idx}.png")
            print(f"  {slug}_p{idx}.png  ({len(part)} lines)")

print("DONE")
