package smartcampus.android.template.standalone.Utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextView extends TextView{
	
		public FontTextView(Context context, AttributeSet attrs, int defStyle) {
		    super(context, attrs, defStyle);
		    init();
		}

		public FontTextView(Context context, AttributeSet attrs) {
		    super(context, attrs);
		    init();
		}

		public FontTextView(Context context) {
		    super(context);
		    init();
		}

		public void init() 
		{
			 if (!isInEditMode()) {
				    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "PatuaOne-Regular.otf");
				    setTypeface(tf);
			 }
		}
		
}
