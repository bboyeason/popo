package lbs;

public class DrawableGameComponent {

	public interface OnStageCompleteListener {
		void OnStageComplete(DrawableGameComponent stage);
	}

	public DrawableGameComponent() {
		
	}
	
	public void Dispose() {
		UnloadContent();
	}
	
	protected void Initialize() {
	}
	
	protected void LoadContent(){
	}
	
	protected void UnloadContent() {
		
	}
	
	protected void Update() {
		
	}
	
	protected void Draw() {
		
	}
}
