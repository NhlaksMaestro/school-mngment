-- Create the public.users table
CREATE TABLE IF NOT EXISTS public.users (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  date_of_birth DATE NOT NULL,
  cellphone_number VARCHAR(45) NOT NULL,
  email_address VARCHAR(45) NOT NULL,
  current_score INT NULL,
  average_score INT NULL,
  student_number VARCHAR(95) NULL,
  created_by VARCHAR(45) DEFAULT 'System',
  created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by VARCHAR(45) NULL,
  updated_on TIMESTAMP NULL,
  is_deleted BOOLEAN DEFAULT FALSE
);

-- Create the public.users history table
CREATE TABLE IF NOT EXISTS public.users_history (
  id SERIAL PRIMARY KEY,
  user_id INT,
  first_name VARCHAR(45),
  last_name VARCHAR(45),
  date_of_birth TIMESTAMP,
  cellphone_number VARCHAR(45),
  email_address VARCHAR(45),
  current_score INT,
  average_score INT,
  student_number VARCHAR(95),
  created_by VARCHAR(45),
  created_on TIMESTAMP,
  updated_by VARCHAR(45),
  updated_on TIMESTAMP,
  is_deleted BOOLEAN
);
-- Create the trigger function to update default values
CREATE OR REPLACE FUNCTION update_default_values()
  RETURNS TRIGGER AS $$
  BEGIN
    IF TG_OP = 'UPDATE' THEN
      NEW.updated_on := CURRENT_TIMESTAMP;
      NEW.updated_by := 'System';
    END IF;
    RETURN NEW;
  END;
$$ LANGUAGE plpgsql;

-- Create the trigger to update default values on update
CREATE TRIGGER update_default_values_trigger
  AFTER UPDATE ON public.users
  FOR EACH ROW
  WHEN (OLD.* IS DISTINCT FROM NEW.*)
  EXECUTE FUNCTION update_default_values();

-- Create the trigger function to set default values
CREATE OR REPLACE FUNCTION set_default_student_number()
  RETURNS TRIGGER AS $$
  BEGIN
    IF NEW.student_number IS NULL THEN
      NEW.student_number := CONCAT(NEW.first_name, '-', NEW.last_name, NEW.id);
    END IF;
    IF NEW.is_deleted IS NULL THEN
      NEW.is_deleted := FALSE;
    END IF;
    NEW.created_on := CURRENT_TIMESTAMP;
    NEW.created_by := 'System';
    RETURN NEW;
  END;
$$ LANGUAGE plpgsql;

-- Create the trigger to set default values
CREATE TRIGGER set_student_number_trigger
  BEFORE INSERT ON public.users
  FOR EACH ROW
  WHEN (NEW.student_number IS NULL)
  EXECUTE FUNCTION set_default_student_number();

-- Create the trigger for inserting into public.users_history after update
CREATE OR REPLACE FUNCTION trg_after_update_users()
RETURNS TRIGGER AS $$
BEGIN
  IF (NEW.first_name <> OLD.first_name OR NEW.last_name <> OLD.last_name OR NEW.date_of_birth <> OLD.date_of_birth OR NEW.cellphone_number <> OLD.cellphone_number OR NEW.email_address <> OLD.email_address OR NEW.current_score <> OLD.current_score OR NEW.average_score <> OLD.average_score OR NEW.student_number <> OLD.student_number OR NEW.created_by <> OLD.created_by OR NEW.updated_by <> OLD.updated_by OR NEW.is_deleted <> OLD.is_deleted) THEN
    INSERT INTO public.users_history (user_id, first_name, last_name, date_of_birth, cellphone_number, email_address, current_score, average_score, student_number, created_by, created_on, updated_by, updated_on, is_deleted)
    VALUES (OLD.id, OLD.first_name, OLD.last_name, OLD.date_of_birth, OLD.cellphone_number, OLD.email_address, OLD.current_score, OLD.average_score, OLD.student_number, OLD.created_by, OLD.created_on, OLD.updated_by, OLD.updated_on, OLD.is_deleted);
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_after_update_users
AFTER UPDATE ON public.users
FOR EACH ROW
EXECUTE FUNCTION trg_after_update_users();

-- Create the trigger for inserting intopublic.users_history after delete
CREATE OR REPLACE FUNCTION trg_after_delete_users()
RETURNS TRIGGER AS $$
BEGIN
  INSERT INTO public.users_history (user_id, first_name, last_name, date_of_birth, cellphone_number, email_address, current_score, average_score, student_number, created_by, created_on, updated_by, updated_on, is_deleted)
  VALUES (OLD.id, OLD.first_name, OLD.last_name, OLD.date_of_birth, OLD.cellphone_number, OLD.email_address, OLD.current_score, OLD.average_score, OLD.student_number, OLD.created_by, OLD.created_on, OLD.updated_by, OLD.updated_on, OLD.is_deleted);
  RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_after_delete_users
AFTER DELETE ON public.users
FOR EACH ROW
EXECUTE FUNCTION trg_after_delete_users();

-- Create calculate_average_score the trigger function
CREATE OR REPLACE FUNCTION calculate_average_score()
  RETURNS TRIGGER AS
$$
DECLARE
  total_scores INT;
  total_users INT;
  class_average DECIMAL;
BEGIN
  -- Calculate the total scores of all users
  SELECT SUM(current_score)
  INTO total_scores
  FROM public.users;
  
  -- Calculate the total number of users
  SELECT COUNT(*)
  INTO total_users
  FROM public.users;
  
  -- Calculate the class average
  IF total_users > 0 THEN
    class_average := total_scores::DECIMAL / total_users;
  ELSE
    class_average := 0;
  END IF;
  
  -- Calculate the percentage contribution to the class average
  DECLARE
    percentage DECIMAL;
  BEGIN
    IF class_average > 0 THEN
      percentage := (NEW.current_score / class_average) * 100;
      
      -- Cap the percentage at 100%
      IF percentage > 100 THEN
        percentage := 100;
      END IF;
    ELSE
      percentage := 0;
    END IF;
    
    -- Update the average score for the inserted user
    UPDATE public.users
    SET average_score = percentage  -- Update with percentage instead of class_average
    WHERE id = NEW.id;
  END;
  
  RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create the trigger
CREATE TRIGGER calculate_average_trigger
AFTER INSERT ON public.users
FOR EACH ROW
EXECUTE FUNCTION calculate_average_score();