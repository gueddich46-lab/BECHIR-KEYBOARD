from pathlib import Path
from PIL import Image, ImageDraw
root=Path("app/src/main/res")
def render(size, foreground=False):
    scale=4
    n=size*scale
    im=Image.new("RGBA",(n,n),(23,16,36,0 if foreground else 255))
    d=ImageDraw.Draw(im)
    def rect(box,color,r=0):
        b=tuple(round(x*n/108) for x in box)
        d.rounded_rectangle(b,radius=round(r*n/108),fill=color)
    # Preserve the original compact purple keyboard design and generous padding.
    rect((18,22,90,86),(97,55,160),8)
    for x in (23,39,55,71):
        rect((x,34,x+13,44),(178,140,243),2)
        rect((x,49,x+13,59),(178,140,243),2)
    rect((29,66,79,75),(178,140,243),3)
    return im.resize((size,size),Image.Resampling.LANCZOS)
for density,size in {"mdpi":48,"hdpi":72,"xhdpi":96,"xxhdpi":144,"xxxhdpi":192}.items():
    folder=root/("mipmap-"+density)
    folder.mkdir(parents=True,exist_ok=True)
    render(size).save(folder/"ic_launcher.png")
folder=root/"drawable-nodpi"
folder.mkdir(parents=True,exist_ok=True)
render(432,True).save(folder/"bechir_icon_foreground.png")
