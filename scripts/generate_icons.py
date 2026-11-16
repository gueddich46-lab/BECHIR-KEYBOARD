from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
import math
root=Path("app/src/main/res")
def render(size, foreground=False):
    n=size*4
    im=Image.new("RGBA",(n,n),(0,0,0,0))
    pix=im.load()
    for y in range(n):
        for x in range(n):
            t=y/n
            glow=max(0,1-math.hypot((x/n-.27)*1.1,(y/n-.20)*1.2)*1.4)
            pix[x,y]=(int(43+74*glow+27*(1-t)),int(13+16*glow),int(92+91*glow+35*(1-t)),255)
    d=ImageDraw.Draw(im)
    def box(coords,fill,r=0,outline=None,width=1):
        c=tuple(round(v*n/108) for v in coords)
        d.rounded_rectangle(c,radius=round(r*n/108),fill=fill,outline=outline,width=max(1,round(width*n/108)))
    # New distinct B and keyboard emblem, smaller with generous icon safe-zone.
    box((21,43,87,83),(64,19,116,255),9,outline=(198,126,255,255),width=2)
    for x in [29,43,57,71]:
        box((x,53,x+9,61),(234,209,255,255),2)
        box((x,64,x+9,72),(234,209,255,255),2)
    box((38,76,70,80),(240,219,255,255),2)
    try:
        font=ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf",round(n*.32))
    except OSError:
        font=ImageFont.load_default()
    d.text((n*.50,n*.305),"B",font=font,fill=(255,244,255,255),anchor="mm",stroke_width=round(n*.003),stroke_fill=(235,189,255,255))
    return im.resize((size,size),Image.Resampling.LANCZOS)
for density,size in {"mdpi":48,"hdpi":72,"xhdpi":96,"xxhdpi":144,"xxxhdpi":192}.items():
    folder=root/("mipmap-"+density)
    folder.mkdir(parents=True,exist_ok=True)
    render(size).save(folder/"ic_launcher.png")
folder=root/"drawable-nodpi"
folder.mkdir(parents=True,exist_ok=True)
render(432,True).save(folder/"bechir_icon_foreground.png")
