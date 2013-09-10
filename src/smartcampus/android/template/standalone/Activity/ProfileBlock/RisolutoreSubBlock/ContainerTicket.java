package smartcampus.android.template.standalone.Activity.ProfileBlock.RisolutoreSubBlock;

import android.graphics.Bitmap;

public class ContainerTicket {

	private static Ticket mTicket = null;

	public static void getInstance() {
		mTicket = (mTicket == null) ? new Ticket() : mTicket;
	}

	public static String getmAddress() {
		return mTicket.getmAddress();
	}

	public static void setmAddress(String mAddress) {
		mTicket.setmAddress(mAddress);
	}

	public static String getmDesc() {
		return mTicket.getmDesc();
	}

	public static void setmDesc(String mDesc) {
		mTicket.setmDesc(mDesc);
	}

	public static String getmCategoria() {
		return mTicket.getmCategoria();
	}

	public static void setmCategoria(String mCategoria) {
		mTicket.setmCategoria(mCategoria);
	}

	public static Bitmap getmPhoto() {
		return mTicket.getmPhoto();
	}

	public static void setmPhoto(Bitmap mPhoto) {
		mTicket.setmPhoto(mPhoto);
	}

	private static class Ticket {
		private String mAddress;
		private String mDesc;
		private String mCategoria;
		private Bitmap mPhoto;

		private Ticket(String mAddress, String mDesc, String mCategoria,
				Bitmap mPhoto) {
			super();
			this.mAddress = mAddress;
			this.mDesc = mDesc;
			this.mCategoria = mCategoria;
			this.mPhoto = mPhoto;
		}

		private Ticket() {
			this(null, null, null, null);
		}

		private String getmAddress() {
			return mAddress;
		}

		private void setmAddress(String mAddress) {
			this.mAddress = mAddress;
		}

		private String getmDesc() {
			return mDesc;
		}

		private void setmDesc(String mDesc) {
			this.mDesc = mDesc;
		}

		private String getmCategoria() {
			return mCategoria;
		}

		private void setmCategoria(String mCategoria) {
			this.mCategoria = mCategoria;
		}

		private Bitmap getmPhoto() {
			return mPhoto;
		}

		private void setmPhoto(Bitmap mPhoto) {
			this.mPhoto = mPhoto;
		}
	}
}
